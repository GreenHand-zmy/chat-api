package org.peter.chat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.entity.User;
import org.peter.chat.exception.ChatCheckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test(expected = ChatCheckException.class)
    public void queryUsernameIsExist() {
        // ===================会抛出检查异常===========================
        userService.queryUsernameIsExist("");

        // ===================用户名存在===========================
        String username = "test";
        boolean result = userService.queryUsernameIsExist(username);
        assertTrue(result);

        // ===================用户名不存在===========================
        username = "notExist";
        result = userService.queryUsernameIsExist(username);
        assertTrue(!result);
    }

    @Test(expected = ChatCheckException.class)
    public void queryUserForLogin() {
        // ====================会抛出检查异常=========================
        userService.queryUserForLogin("", "");

        User result = userService.queryUserForLogin("test", "123");
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void registerUser() {
        User user = new User();
        user.setUsername("zmy")
                .setPassword("123")
                .setNickname("zmy");

        userService.registerUser(user);
    }

}