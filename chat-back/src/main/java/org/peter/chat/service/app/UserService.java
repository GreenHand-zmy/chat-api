package org.peter.chat.service.app;

import org.peter.chat.domain.bo.FriendRequestBO;
import org.peter.chat.domain.bo.query.UserSearchQuery;
import org.peter.chat.domain.vo.UserWithTokenVO;
import org.peter.chat.domain.vo.common.FriendRequestVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;

import java.util.List;

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
    UserWithTokenVO userLogin(String username, String password);

    /**
     * 用户注册
     *
     * @return
     */
    UserWithTokenVO userRegister(UserEntity user);

    /**
     * 通过token查找用户
     *
     * @param token
     * @return
     */
    UserCommonVO queryByToken(String token);


    /**
     * 通过用户编号查找用户
     *
     * @param userId
     * @return
     */
    UserCommonVO queryById(String userId);

    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return
     */
    UserCommonVO queryByUsername(String username);

    /**
     * 通过参数查找用户
     *
     * @param searchQuery
     * @return
     */
    UserCommonVO queryByParams(UserSearchQuery searchQuery);

    /**
     * 判断两个用户是否好友关系
     *
     * @param oneUserId
     * @param otherUserId
     * @return
     */
    Boolean isHavingRelation(String oneUserId, String otherUserId);

    /**
     * 通过自身用户编号查询好友列表
     *
     * @param myUserId
     * @return
     */
    List<UserCommonVO> queryFriendListByMyUserId(String myUserId);

    /**
     * 通过用户编号修改用户信息
     *
     * @param user
     * @return
     */
    UserCommonVO updateById(UserEntity user);

    /**
     * 通过用户名和旧密码重置新密码
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    UserWithTokenVO resetPassword(String username, String oldPassword, String newPassword);

    /**
     * 发送好友请求
     *
     * @param myUserId
     * @param friendUserId
     */
    void sendFriendRequest(String myUserId, String friendUserId);

    /**
     * 通过用户编号查询未处理的好友请求
     *
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 通过好友请求
     *
     * @param myUserId
     * @param friendRequest
     */
    void acceptFriendRequest(String myUserId, FriendRequestBO friendRequest);

    /**
     * 拒绝好友请求
     *
     * @param myUserId
     * @param friendRequest
     */
    void rejectFriendRequest(String myUserId, FriendRequestBO friendRequest);

    /**
     * 删除好友
     *
     * @param meId
     * @param friendId
     */
    void removeFriend(String meId, String friendId);
}
