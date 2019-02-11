package org.peter.chat.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.mapper.qo.ChatHistoryMapperQO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatHistoryMapperTest {
    @Autowired
    private ChatHistoryMapper chatHistoryMapper;

    @Test
    public void batchUpdateHistorySign() {
        List<String> idList = new ArrayList<>();
        idList.add("1901188NH8325P00");
        idList.add("1901188P84HG9X68");
        idList.add("1901188P9FCXSMCH");

        chatHistoryMapper.batchUpdateHistorySign(idList);
    }

    @Test
    public void queryHistory() {
        ChatHistoryMapperQO query = new ChatHistoryMapperQO();
        query.setOneUserId("181116GNPNZ0RZ54")
                .setOtherUserId("181118DZ3XBANZMW");
        Page page = new Page();
        page.setSize(20)
                .setDesc("gmt_created")
                .setCurrent(1);
        IPage<ChatHistoryEntity> queryHistory = chatHistoryMapper.queryHistory(page, query);

        System.out.println(queryHistory);
    }
}