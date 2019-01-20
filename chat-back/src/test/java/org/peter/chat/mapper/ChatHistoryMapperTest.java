package org.peter.chat.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
}