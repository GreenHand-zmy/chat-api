package org.peter.chat.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ChatHistoryVO {
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
