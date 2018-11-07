package org.peter.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.peter.chat.config.properties.ChatProperties;
import org.peter.chat.domain.vo.UserVo;
import org.peter.chat.entity.User;
import org.peter.chat.enums.ChatStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.mapper.UserMapper;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.Md5Util;
import org.peter.chat.utils.SecureRandoms;
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
    public UserVo userLogin(String username, String password) {
        // 使用md5(password + username)
        String passwordDigest = Md5Util.md5Str(password, username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", passwordDigest);

        User result = userMapper.selectOne(wrapper);
        if (result == null) {
            throw new BusinessException(ChatStatus.ACCOUNT_OR_PASSWORD_ERROR,
                    "username={}", username);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(result, userVo);

        log.info("username={} 登陆成功", userVo.getUsername());
        return userVo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserVo userRegister(User user) {
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
        String token = SecureRandoms.nextHex(64);
        user.setToken(token);

        // 使用md5(password + username(slat))作为密码
        user.setPassword(Md5Util.md5Str(user.getPassword()
                , user.getUsername()));

        // 设置用户属性
        user.setNickname(chatProperties.getRandomNickname()); //后期从配置文件中获取
        user.setFaceImage("");
        user.setFaceImageBig("");
        user.setQrcode("");
        user.setGmtCreated(LocalDateTime.now());
        userMapper.insert(user);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);

        log.info("新用户username={} 注册成功", userVo.getUsername());
        return userVo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserVo queryByToken(String token) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("token", token);
        User result = userMapper.selectOne(wrapper);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(result, userVo);
        return userVo;
    }

    @Override
    public UserVo queryById(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ChatStatus.INVALID_USER_ID,
                    "userId={}", userId);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public UserVo updateById(UserVo user) {
        return null;
    }
}
