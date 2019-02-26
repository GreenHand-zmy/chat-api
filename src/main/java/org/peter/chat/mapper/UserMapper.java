package org.peter.chat.mapper;

import org.apache.ibatis.annotations.Param;
import org.peter.chat.domain.vo.FriendRequestVO;
import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.peter.chat.enums.FriendRequestStatus;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zmy
 * @since 2018-10-14
 */
public interface UserMapper extends BaseMapper<UserEntity> {
    /**
     * 通过用户编号查询好友请求
     *
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(@Param("acceptUserId") String acceptUserId
            , @Param("status") FriendRequestStatus status);

    /**
     * 通过用户编号查询好友列表
     *
     * @param myUserId
     * @return
     */
    List<UserCommonVO> queryFriendList(String myUserId);
}
