package org.peter.chat.controller;

import io.swagger.annotations.Api;
import org.peter.chat.domain.vo.UnReadMsgVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.service.app.UserService;
import org.peter.chat.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Api("聊天业务controller")
public class ChatController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatHistoryService chatHistoryService;

/*    *//**
     * 查询用户未读信息
     *
     * @return
     *//*
    @GetMapping("/unReadMsg")
    ResultBean<List<UnReadMsgVO>> getUnReadMsg() {
        UserCommonVO session = SessionController.getCurrentUserSession();
        List<UnReadMsgVO> unReadMsgVOList = chatHistoryService.queryUnReadMsgByUserId(session.getId());
        return new ResultBean<List<UnReadMsgVO>>().success(unReadMsgVOList);
    }

    *//**
     * 返回多个好友的最新的一条消息
     *
     * @return
     *//*
    @GetMapping("/lastUnReadMsg")
    ResultBean<List<UnReadMsgVO>> getLastUnReadMsg() {
        UserCommonVO session = SessionController.getCurrentUserSession();
        List<UnReadMsgVO> unReadMsgVOList = chatHistoryService.queryUnReadMsgByUserId(session.getId());
        return new ResultBean<List<UnReadMsgVO>>().success(unReadMsgVOList);
    }*/
}
