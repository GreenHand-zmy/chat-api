package org.peter.chat.service.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.domain.qo.HistorySearchQO;
import org.peter.chat.domain.qo.common.PageQO;
import org.peter.chat.domain.vo.ChatHistoryVO;
import org.peter.chat.domain.vo.common.PageVO;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.mapper.qo.ChatHistoryMapperQO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatHistoryServiceTest {
    @Autowired
    private ChatHistoryService chatHistoryService;

    @Test
    public void save() {
    }

    @Test
    public void batchSignMessage() {
    }

    @Test
    public void queryUnReadMsgByUserId() {
        String userId = "181118DZ3XBANZMW";
        List<ChatHistoryEntity> unReadMsgVOList = chatHistoryService.queryUnReadMsgByUserId(userId);
        unReadMsgVOList.forEach(System.out::println);
    }

    @Test
    public void countUnReadMsg() {
        String userId = "181118DZ3XBANZMW";
        System.out.println(chatHistoryService.countUnReadMsg(userId));
    }

    @Test
    public void queryChatHistory() {
        PageQO<ChatHistoryMapperQO> pageQO = new PageQO<>();

        ChatHistoryMapperQO condition = new ChatHistoryMapperQO();
        condition.setOneUserId("181116GNPNZ0RZ54")
                .setOtherUserId("181118DZ3XBANZMW");

        pageQO.setPageNum(1)
                .setPageSize(20)
                .setDesc("gmt_created")
                .setCondition(condition);
        PageVO<ChatHistoryVO> pageVO = chatHistoryService.queryChatHistory(pageQO);

        System.out.println(pageVO);
    }
}