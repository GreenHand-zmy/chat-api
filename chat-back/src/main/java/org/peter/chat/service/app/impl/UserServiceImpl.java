package org.peter.chat.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.peter.chat.config.properties.ChatProperties;
import org.peter.chat.domain.bo.FriendRequestBO;
import org.peter.chat.domain.bo.query.UserSearchQuery;
import org.peter.chat.domain.vo.UserWithTokenVO;
import org.peter.chat.domain.vo.common.FriendRequestVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.FriendsRelationEntity;
import org.peter.chat.entity.FriendsRequestEntity;
import org.peter.chat.entity.UserEntity;
import org.peter.chat.enums.FriendRequestStatus;
import org.peter.chat.enums.exceptionStatus.FriendRequestExceptionStatus;
import org.peter.chat.enums.exceptionStatus.UserExceptionStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.mapper.FriendsRelationMapper;
import org.peter.chat.mapper.FriendsRequestMapper;
import org.peter.chat.mapper.UserMapper;
import org.peter.chat.service.ServiceThreadPool;
import org.peter.chat.service.app.QRCodeService;
import org.peter.chat.service.app.UserService;
import org.peter.chat.utils.Md5Util;
import org.peter.chat.utils.SecureRandoms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FriendsRelationMapper friendsRelationMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ChatProperties chatProperties;

    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private ServiceThreadPool serviceThreadPool;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        UserEntity result = userMapper.selectOne(wrapper);

        return result != null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserWithTokenVO userLogin(String username, String password) {
        // 使用md5(password + username)
        String passwordDigest = getEncryptionPassword(password);
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", passwordDigest);

        UserEntity result = userMapper.selectOne(wrapper);
        if (result == null) {
            throw new BusinessException(UserExceptionStatus.ACCOUNT_OR_PASSWORD_ERROR,
                    "username={}", username);
        }

        UserWithTokenVO userWithTokenVO = new UserWithTokenVO();
        BeanUtils.copyProperties(result, userWithTokenVO);

        log.info("username={} 登陆成功", userWithTokenVO.getUsername());
        return userWithTokenVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserWithTokenVO userRegister(UserEntity user) {
        // 判断用户名是否已经存在,如果存在抛出异常
        boolean usernameIsExist = queryUsernameIsExist(user.getUsername());
        if (usernameIsExist) {
            throw new BusinessException(UserExceptionStatus.USERNAME_EXISTED_EXCEPTION,
                    "username={}", user.getUsername());
        }

        // 获取主键id
        String userId = sid.nextShort();
        user.setId(userId);

        // 设置用户token
        user.setToken(generateToken());

        // 使用md5(password + username(slat))作为密码
        user.setPassword(getEncryptionPassword(user.getPassword()));

        // 设置用户属性
        user.setNickname(chatProperties.getRandomNickname()); //后期从配置文件中获取
        user.setFaceImage("");
        user.setFaceImageBig("");
        user.setQrcode("");

        user.setGmtCreated(LocalDateTime.now());
        userMapper.insert(user);

        // 异步设置QRcode 为保证注册服务可用性,生成二维码这里可以提交给其他线程去做,防止上传超时抛出异常,导致数据库回滚注册不了
        serviceThreadPool.submit(() -> {
            QrCodeContent qrCodeContent = new QrCodeContent();
            qrCodeContent.setUserId(user.getId());
            String qrContent = JSON.toJSONString(qrCodeContent);
            String qrFilename = user.getId() + "qrcode.png";
            String serverQrcodeFilePath = qrCodeService.generateQrCodeAndUpload(qrFilename, qrContent);

            UserEntity userUpdater = new UserEntity();
            userUpdater.setId(user.getId())
                    .setQrcode(serverQrcodeFilePath);

            userMapper.updateById(userUpdater);
            log.info("新用户设置二维码成功");
        });

        UserWithTokenVO userWithTokenVO = new UserWithTokenVO();
        BeanUtils.copyProperties(user, userWithTokenVO);

        log.info("新用户username={} 注册成功", userWithTokenVO.getUsername());
        return userWithTokenVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserCommonVO queryByToken(String token) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("token", token);
        UserEntity result = userMapper.selectOne(wrapper);

        if (result == null) {
            return null;
        }

        UserCommonVO userCommonVO = new UserCommonVO();
        BeanUtils.copyProperties(result, userCommonVO);
        return userCommonVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserCommonVO queryById(String userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(UserExceptionStatus.INVALID_USER_ID,
                    "userId={}", userId);
        }

        UserCommonVO userCommonVO = new UserCommonVO();
        BeanUtils.copyProperties(user, userCommonVO);
        return userCommonVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserCommonVO queryByUsername(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);

        UserEntity result = userMapper.selectOne(wrapper);
        if (result == null) {
            throw new BusinessException(UserExceptionStatus.USER_NOT_EXISTED_EXCEPTION,
                    "username={}", username);
        }

        UserCommonVO userCommonVO = new UserCommonVO();
        BeanUtils.copyProperties(result, userCommonVO);

        return userCommonVO;
    }

    @Override
    public UserCommonVO queryByParams(UserSearchQuery searchQuery) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(searchQuery.getUserId())) {
            wrapper.eq("id", searchQuery.getUserId());
        }
        if (!StringUtils.isEmpty(searchQuery.getUsername())) {
            wrapper.eq("username", searchQuery.getUsername());
        }

        UserEntity result = userMapper.selectOne(wrapper);
        if (result == null) {
            throw new BusinessException(UserExceptionStatus.USER_NOT_EXISTED_EXCEPTION,
                    "searchQuery={}", searchQuery);
        }
        return UserCommonVO.transTo(result);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Boolean isHavingRelation(String oneUserId, String otherUserId) {
        QueryWrapper<FriendsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("me_user_id", oneUserId)
                .eq("friend_user_id", otherUserId);
        FriendsRelationEntity result = friendsRelationMapper.selectOne(queryWrapper);
        return result != null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserCommonVO> queryFriendListByMyUserId(String myUserId) {
        return userMapper.queryFriendList(myUserId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserCommonVO updateById(UserEntity user) {
        // 根据id更新用户
        user.setGmtModified(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("user={} 更新了个人信息", user);

        // 返回更新后的结果
        return queryById(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserWithTokenVO resetPassword(String username, String oldPassword, String newPassword) {
        String passwordDigest = getEncryptionPassword(oldPassword);

        // 构造查询条件,查询数据库是否存在以username + oldPassword的用户
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", passwordDigest);
        UserEntity user = userMapper.selectOne(wrapper);

        // 用户不存在抛出异常
        if (user == null) {
            throw new BusinessException(UserExceptionStatus.ACCOUNT_OR_PASSWORD_ERROR,
                    "username={},oldPassword={}", username, oldPassword);
        }

        // 设置新密码,新token,并更新到数据库
        String newPasswordDigest = getEncryptionPassword(newPassword);
        String newToken = generateToken();
        user.setPassword(newPasswordDigest)
                .setToken(newToken)
                .setGmtModified(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("user={} 重置了密码", user);

        // 拷贝vo对象
        UserWithTokenVO userWithTokenVO = new UserWithTokenVO();
        BeanUtils.copyProperties(user, userWithTokenVO);
        return userWithTokenVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void sendFriendRequest(String myUserId, String friendUserId) {
        UserCommonVO myUser = queryById(myUserId);
        UserCommonVO friendUser = queryById(friendUserId);

        // 发送的好友不存在
        if (userMapper.selectById(friendUserId) == null) {
            throw new BusinessException(UserExceptionStatus.USER_NOT_EXISTED_EXCEPTION,
                    "myUserId={},friendUserId={}", myUserId, friendUserId);
        }

        // 自己添加自己
        if (myUser.getId().equals(friendUser.getId())) {
            throw new BusinessException(FriendRequestExceptionStatus.INVALID_ADD_USER_REQUEST,
                    "myUserId={},friendUserId={}", myUserId, friendUserId);
        }

        // 查询是否已经为好友
        if (isHavingRelation(myUserId, friendUserId)) {
            throw new BusinessException(FriendRequestExceptionStatus.INVALID_ADD_USER_REQUEST,
                    "myUserId={},friendUserId={}", myUserId, friendUserId);
        }

        // 查询是否已发送好友请求
        QueryWrapper<FriendsRequestEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("send_user_id", myUser.getId())
                .eq("accept_user_id", friendUser.getId())
                .eq("status", FriendRequestStatus.NOT_DISPOSE.getCode());
        FriendsRequestEntity request = friendsRequestMapper.selectOne(wrapper);
        if (request != null) {
            throw new BusinessException(FriendRequestExceptionStatus.REPEAT_ADD_USER_REQUEST,
                    "myUserId={},friendUserId={}", myUserId, friendUserId);
        }

        // 生成好友请求
        FriendsRequestEntity friendsRequest = new FriendsRequestEntity();
        friendsRequest
                .setId(sid.nextShort())
                .setSendUserId(myUser.getId())
                .setAcceptUserId(friendUser.getId())
                .setStatus(FriendRequestStatus.NOT_DISPOSE)
                .setRequestDateTime(LocalDateTime.now());

        friendsRequestMapper.insert(friendsRequest);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return userMapper.queryFriendRequestList(acceptUserId, FriendRequestStatus.NOT_DISPOSE);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void acceptFriendRequest(String myUserId, FriendRequestBO requestBO) {
        // 通过请求编号查找请求
        FriendsRequestEntity friendsRequest = friendsRequestMapper.selectById(requestBO.getRequestId());

        // 处理请求前的处理
        preHandleFriendRequest(myUserId, requestBO, friendsRequest);

        // 确认是否已经为好友关系
        String sendUserId = friendsRequest.getSendUserId();
        if (isHavingRelation(myUserId, sendUserId)) {
            throw new BusinessException(FriendRequestExceptionStatus.INVALID_ADD_USER_REQUEST,
                    "myUserId={},requestBO={}", myUserId, requestBO);
        }

        // 改变添加好友请求状态
        friendsRequest.setStatus(FriendRequestStatus.ACCEPT);
        friendsRequestMapper.updateById(friendsRequest);

        // 处理请求,同意后双方产生关联
        String acceptUserId = friendsRequest.getAcceptUserId();
        combineTwoUser(sendUserId, acceptUserId);
    }

    @Override
    public void rejectFriendRequest(String myUserId, FriendRequestBO requestBO) {
        // 取出该请求
        FriendsRequestEntity friendsRequest = friendsRequestMapper.selectById(requestBO.getRequestId());

        // 处理请求前的处理
        preHandleFriendRequest(myUserId, requestBO, friendsRequest);

        // 标记为被拒绝
        friendsRequest.setStatus(FriendRequestStatus.REJECT);
        friendsRequestMapper.updateById(friendsRequest);

        // todo 后期应该要将拒绝信息推送给请求方
    }

    @Override
    public void removeFriend(String meId, String friendId) {
        // todo 查询是否为好友关系,如果不是则抛出异常
        QueryWrapper<FriendsRelationEntity> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("me_user_id", meId)
                .eq("friend_user_id", friendId);
        FriendsRelationEntity relationOne = friendsRelationMapper.selectOne(wrapper1);

        QueryWrapper<FriendsRelationEntity> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("me_user_id", friendId)
                .eq("friend_user_id", meId);
        FriendsRelationEntity relationTwo = friendsRelationMapper.selectOne(wrapper2);

        List<String> ids = new ArrayList<>();
        ids.add(relationOne.getId());
        ids.add(relationTwo.getId());
        friendsRelationMapper.deleteBatchIds(ids);
    }

    /**
     * 处理请求前需要判断
     * 1:请求是否为空
     * 2:只能处理自己的请求
     * 3:不能处理已经处理过的请求
     *
     * @param myUserId
     * @param requestBO
     * @param friendsRequest
     */
    private void preHandleFriendRequest(String myUserId, FriendRequestBO requestBO,
                                        FriendsRequestEntity friendsRequest) {
        // 通过请求编号查找请求
        if (friendsRequest == null) {
            throw new BusinessException(FriendRequestExceptionStatus.USER_REQUEST_NOT_EXISTED,
                    "requestId={}", requestBO.getRequestId());
        }

        // 只能处理自己的好友请求
        String acceptUserId = friendsRequest.getAcceptUserId();
        if (!acceptUserId.equals(myUserId)) {
            throw new BusinessException(FriendRequestExceptionStatus.ILLEGAL_SOLVE_USER_REQUEST,
                    "requestId={}", requestBO.getRequestId());
        }

        // 如果请求已被处理过,抛出异常
        if (friendsRequest.getStatus().getCode() > FriendRequestStatus.NOT_DISPOSE.getCode()) {
            throw new BusinessException(FriendRequestExceptionStatus.SOLVED_USER_REQUEST,
                    "requestId={}", requestBO.getRequestId());
        }
    }

    /**
     * 使两位用户产生好友联系
     */
    private void combineTwoUser(String userOneId, String userTwoId) {
        // 处理请求,同意后双方产生关联
        FriendsRelationEntity myRelation = new FriendsRelationEntity();
        myRelation.setId(sid.nextShort())
                .setMeUserId(userOneId)
                .setFriendUserId(userTwoId)
                .setGmtCreated(LocalDateTime.now());

        FriendsRelationEntity otherRelation = new FriendsRelationEntity();
        otherRelation.setId(sid.nextShort())
                .setMeUserId(userTwoId)
                .setFriendUserId(userOneId)
                .setGmtCreated(LocalDateTime.now());

        friendsRelationMapper.insert(myRelation);
        friendsRelationMapper.insert(otherRelation);
    }

    private String getEncryptionPassword(String plainPassword) {
        String once = Md5Util.md5Str(plainPassword);
        String twice = Md5Util.md5Str(once);
        return twice;
    }

    private String generateToken() {
        String token = SecureRandoms.nextHex(64);
        return token;
    }

    @Data
    private class QrCodeContent {
        private String userId;
    }
}
