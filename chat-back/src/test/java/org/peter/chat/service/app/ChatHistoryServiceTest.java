package org.peter.chat.service.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.domain.vo.UnReadMsgVO;
import org.peter.chat.entity.ChatHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

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
}