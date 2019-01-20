package org.peter.chat.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.entity.FriendsRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendsRequestMapperTest {
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Test
    public void testGetRequestFromUserId() {
        QueryWrapper<FriendsRequestEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("accept_user_id", "181118DZ3XBANZMW");
        List<FriendsRequestEntity> requestList = friendsRequestMapper.selectList(wrapper);
    }
}