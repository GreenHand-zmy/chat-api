package org.peter.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.peter.chat.config.properties.ChatProperties;
import org.peter.chat.domain.vo.UserVoWithToken;
import org.peter.chat.domain.vo.UserVoWithoutToken;
import org.peter.chat.entity.User;
import org.peter.chat.enums.ChatStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.mapper.UserMapper;
import org.peter.chat.service.QRCodeService;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ChatProperties chatProperties;

    @Autowired
    private QRCodeService qrCodeService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User result = userMapper.selectOne(wrapper);

        return result != null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserVoWithToken userLogin(String username, String password) {
        // 使用md5(password + username)
        String passwordDigest = getEncryptionPassword(password);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", passwordDigest);

        User result = userMapper.selectOne(wrapper);
        if (result == null) {
            throw new BusinessException(ChatStatus.ACCOUNT_OR_PASSWORD_ERROR,
                    "username={}", username);
        }

        UserVoWithToken userVoWithToken = new UserVoWithToken();
        BeanUtils.copyProperties(result, userVoWithToken);

        log.info("username={} 登陆成功", userVoWithToken.getUsername());
        return userVoWithToken;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserVoWithToken userRegister(User user) {
        // 判断用户名是否已经存在,如果存在抛出异常
        boolean usernameIsExist = queryUsernameIsExist(user.getUsername());
        if (usernameIsExist) {
            throw new BusinessException(ChatStatus.USERNAME_EXISTED_EXCEPTION,
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

        // 设置QRcode
        String qrFilename = user.getId() + "qrcode.png";
        String qrContent = "qrcode:[" + user.getId() + "]";
        String serverQrcodeFilePath = qrCodeService.generateQrCodeAndUpload(qrFilename, qrContent);
        user.setQrcode(serverQrcodeFilePath);

        user.setGmtCreated(LocalDateTime.now());
        userMapper.insert(user);

        UserVoWithToken userVoWithToken = new UserVoWithToken();
        BeanUtils.copyProperties(user, userVoWithToken);

        log.info("新用户username={} 注册成功", userVoWithToken.getUsername());
        return userVoWithToken;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserVoWithoutToken queryByToken(String token) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("token", token);
        User result = userMapper.selectOne(wrapper);

        UserVoWithoutToken userVoWithoutToken = new UserVoWithoutToken();
        BeanUtils.copyProperties(result, userVoWithoutToken);
        return userVoWithoutToken;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserVoWithoutToken queryById(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ChatStatus.INVALID_USER_ID,
                    "userId={}", userId);
        }

        UserVoWithoutToken userVoWithoutToken = new UserVoWithoutToken();
        BeanUtils.copyProperties(user, userVoWithoutToken);
        return userVoWithoutToken;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserVoWithoutToken updateById(User user) {
        // 根据id更新用户
        user.setGmtModified(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("user={} 更新了个人信息", user);

        // 返回更新后的结果
        return queryById(user.getId());
    }

    @Override
    public UserVoWithToken resetPassword(String username, String oldPassword, String newPassword) {
        String passwordDigest = getEncryptionPassword(oldPassword);

        // 构造查询条件,查询数据库是否存在以username + oldPassword的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", passwordDigest);
        User user = userMapper.selectOne(wrapper);

        // 用户不存在抛出异常
        if (user == null) {
            throw new BusinessException(ChatStatus.ACCOUNT_OR_PASSWORD_ERROR,
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
        UserVoWithToken userVoWithToken = new UserVoWithToken();
        BeanUtils.copyProperties(user, userVoWithToken);
        return userVoWithToken;
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
}
