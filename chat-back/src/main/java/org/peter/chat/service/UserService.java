package org.peter.chat.service;

import org.peter.chat.domain.vo.UserVo;
import org.peter.chat.entity.User;

public interface UserService {
    /**
     * 判断用户名是否已经存在
     *
     * @param username 用户名
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 通过用户名和密码查询用户
     */
    UserVo userLogin(String username, String password);

    /**
     * 用户注册
     *
     * @return
     */
    UserVo userRegister(User user);

    /**
     * 通过token查找用户
     *
     * @param token
     * @return
     */
    UserVo queryByToken(String token);
}
