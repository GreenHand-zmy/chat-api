package org.peter.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.peter.chat.domain.vo.UserVo;
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
        /*
         * 将用户图片用fastDFS上传到远程服务器,这里可能会出现问题,非法用户可能会提交给不存在的编号
         * 调用此方法会在服务器上生成两份图片文件,一份大图一份缩略图,缩略的高宽配置在配置文件中
         */
        String bigImgServerPath = fastDFSClient.uploadFileAndCrtThumbImage(faceImage);
        String thumbImgServerPath = getThumbImgServerPath(bigImgServerPath);
        System.out.println(bigImgServerPath);
        System.out.println(thumbImgServerPath);

/*        // 通过用户编号获取用户
        UserVo user = userService.queryById(userId);

        // 通过用户编号更新用户
        UserVo userVo = userService.updateById(user);*/


        return new ResultBean().success(thumbImgServerPath);
    }

    private String getThumbImgServerPath(String bigImgServerPath) {
        String[] fileData = bigImgServerPath.split("\\.");
        String filename = fileData[0];
        String extension = fileData[1];

        return filename + "_80x80" + "." + extension;
    }
}
