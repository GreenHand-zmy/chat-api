package org.peter.chat.controller;

import org.peter.chat.domain.vo.UserVo;
import org.peter.chat.entity.User;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registerOrLogin")
    ResultBean registerOrLogin(User user) {
        // 查询用户名是否已经存在
        boolean usernameIsExist = userService
                .queryUsernameIsExist(user.getUsername());

        User userResult = null;
        if (usernameIsExist) {
            // 用户名存在,进行登录逻辑
            userResult = userService.queryUserForLogin(user.getUsername(), user.getPassword());
            if (userResult == null) {
                return new ResultBean<>().failed("用户名或密码错误");
            }
        } else {
            // 进行注册逻辑

        }
        return null;
    }
}
