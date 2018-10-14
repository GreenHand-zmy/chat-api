package org.peter.chat.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
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
public class ChatHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天记录编号
     */
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
