package org.peter.chat.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.domain.vo.FriendRequestVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;
import org.peter.chat.enums.FriendRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectAll() {
        List<UserEntity> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectById() {
        UserEntity user = userMapper.selectById("181116GNPNZ0RZ54");
        System.out.println(user);
    }

    @Test
    public void testQueryFriendRequestList() {
        List<FriendRequestVO> friendRequestVOS = userMapper.queryFriendRequestList("181118DZ3XBANZMW",
                FriendRequestStatus.NOT_DISPOSE);
        System.out.println(friendRequestVOS);
    }

    @Test
    public void queryFriendList() {
        List<UserCommonVO> userCommonVOS = userMapper.queryFriendList("181116GNPNZ0RZ54");
        userCommonVOS.forEach(System.out::println);
    }

}