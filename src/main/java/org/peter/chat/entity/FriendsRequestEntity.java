package org.peter.chat.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.peter.chat.enums.FriendRequestStatus;

/**
 * <p>
 *
 * </p>
 *
 * @author zmy
 * @since 2018-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("friends_request")
public class FriendsRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求编号
     */
    @TableId
    private String id;

    /**
     * 请求方
     */
    private String sendUserId;

    /**
     * 接受方
     */
    private String acceptUserId;

    /**
     * 请求状态
     */
    @TableId
    private FriendRequestStatus status;

    /**
     * 请求时间
     */
    private LocalDateTime requestDateTime;


}
