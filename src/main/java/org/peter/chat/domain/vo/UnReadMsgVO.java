package org.peter.chat.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.entity.ChatHistoryEntity;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 未读消息
 */
@Data
@Accessors(chain = true)
public class UnReadMsgVO {
    /**
     * 发送消息的用户编号
     */
    private String sendUserId;

    /**
     * 消息记录
     */
    private String msg;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 将entity对象装换为VO对象
     *
     * @param chatHistoryEntity
     * @return
     */
    public static UnReadMsgVO transTo(ChatHistoryEntity chatHistoryEntity) {
        UnReadMsgVO unReadMsgVO = new UnReadMsgVO();
        BeanUtils.copyProperties(chatHistoryEntity, unReadMsgVO);

        unReadMsgVO.setSendTime(chatHistoryEntity.getGmtCreated());
        return unReadMsgVO;
    }
}
