package org.peter.chat.controller;

import org.peter.chat.domain.bo.UserBo;
import org.peter.chat.domain.vo.UserVo;
import org.peter.chat.entity.User;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.FileUtils;
import org.peter.chat.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registerOrLogin")
    ResultBean<UserVo> registerOrLogin(@Valid User user) {
        // 查询用户名是否已经存在
        boolean usernameIsExist = userService
                .queryUsernameIsExist(user.getUsername());

        UserVo userResult;
        if (usernameIsExist) {
            // 用户名存在,进行登录逻辑
            userResult = userService.userLogin(user.getUsername(), user.getPassword());
        } else {
            // 进行注册逻辑
            userResult = userService.userRegister(user);
        }
        return new ResultBean<UserVo>().success(userResult);
    }

/*    @PostMapping("/face/upload")
    ResultBean<UserVo> uploadFaceBase64(UserBo userBo) throws Exception {
        // 获取base64字符串
        String faceData = userBo.getFaceData();

        // 临时目录
        String userFacePath = "c:\\" + userBo.getId() + "userface64.png";

        FileUtils.base64ToFile(userFacePath, faceData);


    }*/
}
