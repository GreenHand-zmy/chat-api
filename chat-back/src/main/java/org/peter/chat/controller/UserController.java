package org.peter.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.peter.chat.domain.bo.UserBo;
import org.peter.chat.domain.vo.UserVo;
import org.peter.chat.entity.User;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.FastDFSClient;
import org.peter.chat.utils.FileUtils;
import org.peter.chat.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Soundbank;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/user")
@Api("用户控制器")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;

    @ApiOperation(value = "用户登录或注册")
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

    @ApiOperation("上传用户头像")
    @PutMapping("/{userId}/faceImage")
    ResultBean uploadUserFaceImage(@PathVariable("userId") String userId, MultipartFile faceImage) throws IOException {
        // 将用户图片用fastDFS上传到远程服务器
        String imgServerPath = fastDFSClient.uploadFile(faceImage);

        // 通过用户编号获取用户
        UserVo user = userService.queryById(userId);

        // 通过用户编号更新用户
        UserVo userVo = userService.updateById(user);


        return new ResultBean().success(imgServerPath);
    }

    public static void main(String[] args) {
        String folder = System.getProperty("java.io.tmpdir");
        System.out.println(folder);
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
