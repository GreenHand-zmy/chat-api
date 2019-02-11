package org.peter.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.peter.chat.domain.bo.FriendRequestBO;
import org.peter.chat.domain.bo.UserBO;
import org.peter.chat.domain.bo.UserResetPasswordBO;
import org.peter.chat.domain.bo.UserShowInformationBO;
import org.peter.chat.domain.bo.enums.FriendRequestType;
import org.peter.chat.domain.qo.HistorySearchQO;
import org.peter.chat.domain.qo.UserSearchQO;
import org.peter.chat.domain.qo.common.PageQO;
import org.peter.chat.domain.vo.ChatHistoryVO;
import org.peter.chat.domain.vo.UserWithTokenVO;
import org.peter.chat.domain.vo.FriendRequestVO;
import org.peter.chat.domain.vo.common.PageVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;
import org.peter.chat.enums.exceptionStatus.ChatExceptionStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.mapper.qo.ChatHistoryMapperQO;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.service.app.UserService;
import org.peter.chat.utils.FastDFSClient;
import org.peter.chat.utils.ResultBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.peter.chat.controller.SessionController.getCurrentUserSession;

@RestController
@RequestMapping("/user")
@Api("用户控制器")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private ChatHistoryService chatHistoryService;

    @ApiOperation(value = "用户登录或注册")
    @PostMapping("/registerOrLogin")
    ResultBean<UserWithTokenVO> registerOrLogin(@Valid UserBO user) {
        // 查询用户名是否已经存在
        boolean usernameIsExist = userService
                .queryUsernameIsExist(user.getUsername());

        UserWithTokenVO userResult;
        if (usernameIsExist) {
            // 用户名存在,进行登录逻辑
            userResult = userService.userLogin(user.getUsername(), user.getPassword());
        } else {
            UserEntity record = new UserEntity();
            BeanUtils.copyProperties(user, record);

            // 进行注册逻辑
            userResult = userService.userRegister(record);
        }
        return new ResultBean<UserWithTokenVO>().success(userResult);
    }

    @ApiOperation("通过用户编号获取用户信息")
    @GetMapping("/{userId}")
    ResultBean<UserCommonVO> getUserInfoById(@PathVariable("userId") String userId) {
        UserCommonVO userCommonVO = userService.queryById(userId);
        return new ResultBean<UserCommonVO>().success(userCommonVO);
    }


    @ApiOperation("修改用户常规信息")
    @PutMapping("/information/update")
    ResultBean<UserCommonVO> updateUser(UserShowInformationBO userShowInformationBO) {
        // 获取当前登录的用户
        UserCommonVO currentUserSession = getCurrentUserSession();

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userShowInformationBO, user);
        user.setId(currentUserSession.getId());

        // 通过用户编号更新用户
        UserCommonVO userCommonVO = userService.updateById(user);
        return new ResultBean<UserCommonVO>().success(userCommonVO);
    }


    @ApiOperation("查找用户信息")
    @GetMapping("/search")
    ResultBean<UserCommonVO> getUserInfoByUsername(UserSearchQO searchQuery) {
        // 通过参数查找用户信息
        UserCommonVO userCommonVO = userService.queryByParams(searchQuery);
        return new ResultBean<UserCommonVO>().success(userCommonVO);
    }

    @ApiOperation("查询是否为好友关系")
    @GetMapping("/isFriendRelation")
    ResultBean<Boolean> isFriendRelation(String otherUserId) {
        // 获取当前登录的用户
        UserCommonVO currentUserSession = getCurrentUserSession();

        Boolean relation = userService.isHavingRelation(currentUserSession.getId(), otherUserId);
        return new ResultBean<Boolean>().success(relation);
    }

    @ApiOperation("上传用户头像")
    @PutMapping("/faceImage")
    ResultBean<UserCommonVO> uploadUserFaceImage(@RequestParam("faceImage") MultipartFile faceImage) throws IOException {
        // 获取当前登录的用户
        UserCommonVO currentUserSession = getCurrentUserSession();

        /*
         * 将用户图片用fastDFS上传到远程服务器
         * 调用此方法会在服务器上生成两份图片文件,一份大图一份缩略图,缩略的高宽配置在配置文件中
         */
        String bigImgServerPath = fastDFSClient.uploadFileAndCrtThumbImage(faceImage);
        String thumbImgServerPath = getThumbImgServerPath(bigImgServerPath);

        UserEntity user = new UserEntity();
        user.setId(currentUserSession.getId())
                .setFaceImage(thumbImgServerPath)
                .setFaceImageBig(bigImgServerPath);

        // 通过用户编号更新用户
        UserCommonVO userCommonVO = userService.updateById(user);
        return new ResultBean<UserCommonVO>().success(userCommonVO);
    }

    @ApiOperation("重置用户密码")
    @PostMapping("/resetPassword")
    ResultBean<UserWithTokenVO> resetPassword(@Valid UserResetPasswordBO userResetPasswordBO) {
        UserWithTokenVO userWithTokenVO = userService.resetPassword(userResetPasswordBO.getUsername(),
                userResetPasswordBO.getOldPassword(),
                userResetPasswordBO.getNewPassword());

        return new ResultBean<UserWithTokenVO>().success(userWithTokenVO);
    }

    @ApiOperation("发送添加好友请求")
    @PostMapping("/friendRequest")
    ResultBean addFriend(@RequestParam("acceptUserId") String acceptUserId) {
        UserCommonVO currentUserSession = getCurrentUserSession();

        userService.sendFriendRequest(currentUserSession.getId(), acceptUserId);
        return new ResultBean().success();
    }


    @ApiOperation("用户查询好友请求")
    @GetMapping("/friendRequest")
    ResultBean<List<FriendRequestVO>> getFriendRequest() {
        UserCommonVO currentUserSession = getCurrentUserSession();

        List<FriendRequestVO> friendRequestVOList = userService.queryFriendRequestList(currentUserSession.getId());
        return new ResultBean<List<FriendRequestVO>>().success(friendRequestVOList);
    }

    @ApiOperation("用户处理好友请求")
    @PostMapping("/opt/friendRequest")
    ResultBean optFriendRequest(@Valid FriendRequestBO friendRequest) {
        String userId = getCurrentUserSession().getId();

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

    @ApiOperation("用户查询好友列表")
    @GetMapping("/friendList")
    ResultBean<List<UserCommonVO>> getFriendList() {
        UserCommonVO currentUserSession = getCurrentUserSession();

        List<UserCommonVO> friendList = userService.queryFriendListByMyUserId(currentUserSession.getId());
        return new ResultBean<List<UserCommonVO>>().success(friendList);
    }


    @ApiOperation("用户查询聊天记录")
    @PostMapping("/history")
    ResultBean<PageVO<ChatHistoryVO>> getHistory(@RequestBody PageQO<HistorySearchQO> pageQO) {
        UserCommonVO currentUserSession = getCurrentUserSession();

        HistorySearchQO condition = pageQO.getCondition();
        ChatHistoryMapperQO extra = new ChatHistoryMapperQO();
        extra.setOneUserId(currentUserSession.getId())
                .setOtherUserId(condition.getFriendUserId());

        PageQO<ChatHistoryMapperQO> mapperPageQO = new PageQO<>();
        BeanUtils.copyProperties(pageQO, mapperPageQO);
        mapperPageQO.setCondition(extra);

        PageVO<ChatHistoryVO> result = chatHistoryService.queryChatHistory(mapperPageQO);
        return new ResultBean<PageVO<ChatHistoryVO>>().success(result);
    }

    private String getThumbImgServerPath(String bigImgServerPath) {
        String[] fileData = bigImgServerPath.split("\\.");
        String filename = fileData[0];
        String extension = fileData[1];

        return filename + "_80x80" + "." + extension;
    }
}
