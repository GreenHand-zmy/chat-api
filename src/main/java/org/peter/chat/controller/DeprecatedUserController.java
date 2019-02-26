package org.peter.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.peter.chat.domain.bo.FriendRequestBO;
import org.peter.chat.domain.bo.UserBO;
import org.peter.chat.domain.bo.enums.FriendRequestType;
import org.peter.chat.domain.vo.FriendRequestVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;
import org.peter.chat.enums.exceptionStatus.ChatExceptionStatus;
import org.peter.chat.enums.exceptionStatus.UserExceptionStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.service.app.UserService;
import org.peter.chat.utils.FastDFSClient;
import org.peter.chat.utils.ResultBean;
import org.peter.chat.web.RequestContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 废弃的接口
 * 待删除
 */
@RestController
@RequestMapping("/user")
@Api("用户控制器")
public class DeprecatedUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 已废弃
     *
     * @param userId
     * @param UserBO
     * @return
     */
    @ApiOperation("修改用户常规信息")
    @PutMapping("/{userId}")
    @Deprecated
    ResultBean<UserCommonVO> updateUser_deprecated(@PathVariable("userId") String userId,
                                                   UserBO UserBO) {
        currentUserAllowOnly(userId);

        UserEntity user = new UserEntity();
        user.setId(userId)
                .setNickname(UserBO.getNickname());

        // 通过用户编号更新用户
        UserCommonVO userCommonVO = userService.updateById(user);
        return new ResultBean<UserCommonVO>().success(userCommonVO);
    }

    /**
     * 此api有安全问题，路径中带有uid，
     * 这样的话有人能够用其他人的uid进行上传头像
     *
     * @param userId
     * @param faceImage
     * @return
     * @throws IOException
     */
    @ApiOperation("上传用户头像")
    @PutMapping("/{userId}/faceImage")
    @Deprecated
    ResultBean<UserCommonVO> uploadUserFaceImage_deprecated(@PathVariable("userId") String userId,
                                                            @RequestParam("faceImage") MultipartFile faceImage) throws IOException {
        currentUserAllowOnly(userId);

        /*
         * 将用户图片用fastDFS上传到远程服务器,这里可能会出现问题,非法用户可能会提交给不存在的编号
         * 调用此方法会在服务器上生成两份图片文件,一份大图一份缩略图,缩略的高宽配置在配置文件中
         */
        String bigImgServerPath = fastDFSClient.uploadFileAndCrtThumbImage(faceImage);
        String thumbImgServerPath = getThumbImgServerPath(bigImgServerPath);

        UserEntity user = new UserEntity();
        user.setId(userId)
                .setFaceImage(thumbImgServerPath)
                .setFaceImageBig(bigImgServerPath);

        // 通过用户编号更新用户
        UserCommonVO userCommonVO = userService.updateById(user);
        return new ResultBean<UserCommonVO>().success(userCommonVO);
    }



    @ApiOperation("发送添加好友请求")
    @PostMapping("/{userId}/friendRequest")
    @Deprecated
    ResultBean addFriend_deprecated(@PathVariable("userId") String userId,
                                    @RequestParam("acceptUserId") String acceptUserId) {
        // 只允许用户使用自己id进入系统
        currentUserAllowOnly(userId);

        userService.sendFriendRequest(userId, acceptUserId);
        return new ResultBean().success();
    }

    @ApiOperation("用户查询好友请求")
    @GetMapping("/{userId}/friendRequest")
    ResultBean<List<FriendRequestVO>> getFriendRequest(@PathVariable("userId") String userId) {
        // 只允许用户使用自己id进入系统
        currentUserAllowOnly(userId);

        List<FriendRequestVO> friendRequestVOList = userService.queryFriendRequestList(userId);
        return new ResultBean<List<FriendRequestVO>>().success(friendRequestVOList);
    }

    @ApiOperation("用户查询好友列表")
    @GetMapping("/{userId}/friendList")
    ResultBean<List<UserCommonVO>> getFriendList(@PathVariable("userId") String userId) {
        // 只允许用户使用自己id进入系统
        currentUserAllowOnly(userId);

        List<UserCommonVO> friendList = userService.queryFriendListByMyUserId(userId);
        return new ResultBean<List<UserCommonVO>>().success(friendList);
    }

    @ApiOperation("用户添加好友到好友列表中")
    @PostMapping("/{userId}/friendList")
    ResultBean addNewFriendIntoList(@PathVariable("userId") String userId,
                                    @Valid FriendRequestBO friendRequest) {
        // 只允许用户使用自己id进入系统
        currentUserAllowOnly(userId);

        if (FriendRequestType.ACCEPT.equals(friendRequest.getStatus())) {
            // 通过好友请求
            userService.acceptFriendRequest(userId, friendRequest);
        } else if (FriendRequestType.REJECT.equals(friendRequest.getStatus())) {
            // 拒绝好友请求
            userService.rejectFriendRequest(userId, friendRequest);
        } else {
            // 不处理其他未知好友请求类型
            throw new BusinessException(ChatExceptionStatus.APP_RUNTIME_EXCEPTION);
        }

        return new ResultBean().success();
    }

    /**
     * 只允许当前用户访问自己部分的数据
     *
     * @param userId
     */
    private void currentUserAllowOnly(String userId) {
        // 获取当前用户
        UserCommonVO currentUser = RequestContextHolder.get();
        // 如果当前用户编号与将访问的用户编号不一致
        if (!currentUser.getId().equals(userId)) {
            throw new BusinessException(UserExceptionStatus.ILLEGAL_USER_ACCESS,
                    "currentUserId={},accessUserId={}", currentUser.getId(), userId);
        }
    }

    private String getThumbImgServerPath(String bigImgServerPath) {
        String[] fileData = bigImgServerPath.split("\\.");
        String filename = fileData[0];
        String extension = fileData[1];

        return filename + "_80x80" + "." + extension;
    }
}
