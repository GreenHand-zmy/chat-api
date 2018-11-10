package org.peter.chat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.domain.vo.UserVoWithToken;
import org.peter.chat.domain.vo.UserVoWithoutToken;
import org.peter.chat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void queryUsernameIsExist() {
        // ===================用户名存在===========================
        String username = "test";
        boolean result = userService.queryUsernameIsExist(username);
        assertTrue(result);

        // ===================用户名不存在===========================
        username = "notExist";
        result = userService.queryUsernameIsExist(username);
        assertTrue(!result);
    }

    @Test
    public void userLoginSuccess() {
        // =====================正常登录==============================
        UserVoWithToken result = userService.userLogin("admin", "123");
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void userLoginFail() {
        // =====================账号密码错误===========================
        UserVoWithToken result = userService.userLogin("xxxxx", "1234");
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void registerUserSuccess() {
        User user = new User();
        user.setUsername("admin")
                .setPassword("123")
                .setNickname("zmy");

        userService.userRegister(user);
    }

    @Test
    public void registerUserFail() {
        User user = new User();
        user.setUsername("xxxxx")
                .setPassword("123")
                .setNickname("zmy");

        userService.userRegister(user);
    }

    @Test
    public void queryByToken() {
        UserVoWithoutToken userVoWithoutToken = userService.queryByToken("107B73E466230373B15C71B323AB8362BB558B0EF476FBEF685F63D25A694B6E");
        assertNotNull(userVoWithoutToken);
        System.out.println(userVoWithoutToken);
    }

    @Test
    public void updateById() {
        User user = new User();
        user.setId("18110578Y44MPWDP")
                .setFaceImageBig("test_big")
                .setFaceImage("test_smaill");

        UserVoWithoutToken userVoWithoutToken = userService.updateById(user);
        System.out.println(userVoWithoutToken);
    }

    @Test
    public void resetPassword() {
        User user = new User();
        user.setUsername("admin")
                .setPassword("123");
        String newPassword = "123456";
        UserVoWithToken userVoWithToken = userService.resetPassword(user.getUsername(), user.getPassword(), newPassword);

        System.out.println(userVoWithToken);
    }
}