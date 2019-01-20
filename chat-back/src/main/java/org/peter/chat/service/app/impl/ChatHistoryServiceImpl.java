package org.peter.chat.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.n3r.idworker.Sid;
import org.peter.chat.domain.vo.UnReadMsgVO;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.enums.MsgStatus;
import org.peter.chat.mapper.ChatHistoryMapper;
import org.peter.chat.service.app.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {
    @Autowired
    private Sid sid;

    @Autowired
    private ChatHistoryMapper chatHistoryMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ChatHistoryEntity chatHistoryEntity) {
        chatHistoryEntity
                .setId(sid.nextShort())
                .setSignFlag(MsgStatus.NOT_SIGN.getCode())
                .setGmtCreated(LocalDateTime.now());
        chatHistoryMapper.insert(chatHistoryEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchSignMessage(List<String> messageIdList) {
        chatHistoryMapper.batchUpdateHistorySign(messageIdList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ChatHistoryEntity> queryUnReadMsgByUserId(String userId) {
        QueryWrapper<ChatHistoryEntity> query = new QueryWrapper<>();
        query.eq("accept_user_id", userId)
                .eq("sign_flag", MsgStatus.NOT_SIGN.getCode());

        return chatHistoryMapper
                .selectList(query);
    }

    @Override
    public Integer countUnReadMsg(String userId) {
        QueryWrapper<ChatHistoryEntity> query = new QueryWrapper<>();
        query.eq("accept_user_id", userId)
                .eq("sign_flag", MsgStatus.NOT_SIGN.getCode());

        return chatHistoryMapper.selectCount(query);
    }
}
