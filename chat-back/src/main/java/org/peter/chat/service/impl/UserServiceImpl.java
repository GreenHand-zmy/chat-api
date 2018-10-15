package org.peter.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.peter.chat.domain.vo.UserVo;
import org.peter.chat.entity.User;
import org.peter.chat.mapper.UserMapper;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.CheckUtil;
import org.peter.chat.utils.Md5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        CheckUtil.check(StringUtils.isNotBlank(username)
                , "用户名不能为空");

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User result = userMapper.selectOne(wrapper);

        return result != null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserForLogin(String username, String password) {
        CheckUtil.check(StringUtils.isNotBlank(username) &&
                StringUtils.isNotBlank(password), "用户名和密码不能为空");

        // 使用md5(password + username)
        String passwordDigest = Md5Util.md5Str(password, username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("password", passwordDigest);

        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserVo registerUser(User user) {
        // 获取id
        String userId = sid.nextShort();
        user.setId(userId);

        // 使用md5(password + username(slat))作为密码
        user.setPassword(Md5Util.md5Str(user.getPassword()
                , user.getUsername()));

        user.setFaceImage("");
        user.setFaceImageBig("");
        user.setQrcode("");
        user.setGmtCreated(LocalDateTime.now());
        userMapper.insert(user);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
