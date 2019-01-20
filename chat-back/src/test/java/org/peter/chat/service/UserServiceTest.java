package org.peter.chat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.domain.bo.FriendRequestBO;
import org.peter.chat.domain.vo.UserWithTokenVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;
import org.peter.chat.service.app.UserService;
import org.peter.chat.service.opt.OptUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private OptUserService optUserService;

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
        UserWithTokenVO result = userService.userLogin("admin", "123");
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void userLoginFail() {
        // =====================账号密码错误===========================
        UserWithTokenVO result = userService.userLogin("xxxxx", "1234");
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void registerUserSuccess() {
        UserEntity user = new UserEntity();
        user.setUsername("admin")
                .setPassword("123")
                .setNickname("zmy");

        userService.userRegister(user);
    }

    @Test
    public void registerUserFail() {
        UserEntity user = new UserEntity();
        user.setUsername("xxxxx")
                .setPassword("123")
                .setNickname("zmy");

        userService.userRegister(user);
    }

    @Test
    public void queryByToken() {
        UserCommonVO userCommonVO = userService.queryByToken("107B73E466230373B15C71B323AB8362BB558B0EF476FBEF685F63D25A694B6E");
        assertNotNull(userCommonVO);
        System.out.println(userCommonVO);
    }

    @Test
    public void updateById() {
        UserEntity user = new UserEntity();
        user.setId("18110578Y44MPWDP")
                .setFaceImageBig("test_big")
                .setFaceImage("test_smaill");

        UserCommonVO userCommonVO = userService.updateById(user);
        System.out.println(userCommonVO);
    }

    @Test
    public void resetPassword() {
        UserEntity user = new UserEntity();
        user.setUsername("admin")
                .setPassword("123");
        String newPassword = "123456";
        UserWithTokenVO userWithTokenVO = userService.resetPassword(user.getUsername(), user.getPassword(), newPassword);

        System.out.println(userWithTokenVO);
    }

    @Test
    public void sendFriendRequest() {
        String meUserId = userService.queryByUsername("zmyqw").getId();
        String otherUserId = userService.queryByUsername("zmyqwer").getId();


        userService.sendFriendRequest(otherUserId, meUserId);
    }

    @Test
    public void acceptFriendRequest() {
        String myUserId = "181118DZ3XBANZMW";
        FriendRequestBO requestBo = new FriendRequestBO();
        requestBo.setRequestId("181119AKG0Z7DW6W");

        userService.acceptFriendRequest(myUserId, requestBo);
    }

    @Test
    public void rejectFriendRequest() {
        String myUserId = "181118DZ3XBANZMW";
        FriendRequestBO requestBo = new FriendRequestBO();
        requestBo.setRequestId("181119BSK07MGBMW");

        userService.rejectFriendRequest(myUserId, requestBo);
    }

    @Test
    public void mockFriendRequest() {
        UserCommonVO currentUser = userService.queryByUsername("zmyqw");

        List<UserCommonVO> userCommonVOList = optUserService.queryAll();
        for (UserCommonVO userCommonVO : userCommonVOList) {
            if (!userCommonVO.getUsername().equals(currentUser.getUsername())) {
                userService.sendFriendRequest(userCommonVO.getId(), currentUser.getId());
            }
        }
    }

}