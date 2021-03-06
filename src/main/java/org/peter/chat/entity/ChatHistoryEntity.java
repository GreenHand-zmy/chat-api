package org.peter.chat.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("chat_history")
public class ChatHistoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天记录编号
     */
    @TableId
    private String id;

    /**
     * 发送消息的用户编号
     */
    private String sendUserId;

    /**
     * 接受消息的用户编号
     */
    private String acceptUserId;

    /**
     * 消息记录
     */
    private String msg;

    /**
     * 是否已读
     */
    private Integer signFlag;

    /**
     * 记录产生时间
     */
    private LocalDateTime gmtCreated;


}
