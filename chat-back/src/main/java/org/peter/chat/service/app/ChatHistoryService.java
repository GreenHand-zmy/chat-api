package org.peter.chat.service.app;

import org.peter.chat.domain.vo.UnReadMsgVO;
import org.peter.chat.entity.ChatHistoryEntity;

import java.util.List;

public interface ChatHistoryService {
    /**
     * 保存一条聊天记录
     *
     * @param chatHistoryEntity
     */
    void save(ChatHistoryEntity chatHistoryEntity);

    /**
     * 批量签收聊天记录
     *
     * @param messageIdList
     */
    void batchSignMessage(List<String> messageIdList);

    /**
     * 查询未读的好友消息
     *
     * @param userId
     * @return
     */
    List<ChatHistoryEntity> queryUnReadMsgByUserId(String userId);

    /**
     * 查询未读的消息数量
     *
     * @param userId
     * @return
     */
    Integer countUnReadMsg(String userId);
}
