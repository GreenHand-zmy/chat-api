package org.peter.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.peter.chat.domain.bo.UserBo;
import org.peter.chat.domain.bo.UserResetPasswordBo;
import org.peter.chat.domain.vo.UserVoWithToken;
import org.peter.chat.domain.vo.UserVoWithoutToken;
import org.peter.chat.entity.User;
import org.peter.chat.service.UserService;
import org.peter.chat.utils.FastDFSClient;
import org.peter.chat.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    ResultBean<UserVoWithToken> registerOrLogin(@Valid User user) {
        // 查询用户名是否已经存在
        boolean usernameIsExist = userService
                .queryUsernameIsExist(user.getUsername());

        UserVoWithToken userResult;
        if (usernameIsExist) {
            // 用户名存在,进行登录逻辑
            userResult = userService.userLogin(user.getUsername(), user.getPassword());
        } else {
            // 进行注册逻辑
            userResult = userService.userRegister(user);
        }
        return new ResultBean<UserVoWithToken>().success(userResult);
    }

    @ApiOperation("通过用户编号获取用户信息")
    @GetMapping("/{userId}")
    ResultBean<UserVoWithoutToken> getUserInfoById(@PathVariable("userId") String userId) {
        UserVoWithoutToken userVoWithoutToken = userService.queryById(userId);
        return new ResultBean<UserVoWithoutToken>().success(userVoWithoutToken);
    }

    @ApiOperation("上传用户头像")
    @PutMapping("/{userId}/faceImage")
    ResultBean<UserVoWithoutToken> uploadUserFaceImage(@PathVariable("userId") String userId,
                                                       @RequestParam("faceImage") MultipartFile faceImage) throws IOException {
        /*
         * 将用户图片用fastDFS上传到远程服务器,这里可能会出现问题,非法用户可能会提交给不存在的编号
         * 调用此方法会在服务器上生成两份图片文件,一份大图一份缩略图,缩略的高宽配置在配置文件中
         */
        String bigImgServerPath = fastDFSClient.uploadFileAndCrtThumbImage(faceImage);
        String thumbImgServerPath = getThumbImgServerPath(bigImgServerPath);

        User user = new User();
        user.setId(userId)
                .setFaceImage(thumbImgServerPath)
                .setFaceImageBig(bigImgServerPath);

        // 通过用户编号更新用户
        UserVoWithoutToken userVoWithoutToken = userService.updateById(user);
        return new ResultBean<UserVoWithoutToken>().success(userVoWithoutToken);
    }

    @ApiOperation("修改用户常规信息")
    @PutMapping("/{userId}")
    ResultBean<UserVoWithoutToken> updateUser(@PathVariable("userId") String userId,
                                              UserBo UserBo) {
        User user = new User();
        user.setId(userId)
                .setNickname(UserBo.getNickname());

        // 通过用户编号更新用户
        UserVoWithoutToken userVoWithoutToken = userService.updateById(user);
        return new ResultBean<UserVoWithoutToken>().success(userVoWithoutToken);
    }

    @ApiOperation("重置用户密码")
    @PostMapping("/resetPassword")
    ResultBean<UserVoWithToken> resetPassword(@Valid UserResetPasswordBo userResetPasswordBo) {
        // 通过用户编号更新用户
        UserVoWithToken userVoWithToken = userService.resetPassword(userResetPasswordBo.getUsername(),
                userResetPasswordBo.getOldPassword(),
                userResetPasswordBo.getNewPassword());

        return new ResultBean<UserVoWithToken>().success(userVoWithToken);
    }

    private String getThumbImgServerPath(String bigImgServerPath) {
        String[] fileData = bigImgServerPath.split("\\.");
        String filename = fileData[0];
        String extension = fileData[1];

        return filename + "_80x80" + "." + extension;
    }
}
