package org.peter.chat.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.n3r.idworker.Sid;
import org.peter.chat.domain.qo.HistorySearchQO;
import org.peter.chat.domain.qo.common.PageQO;
import org.peter.chat.domain.vo.ChatHistoryVO;
import org.peter.chat.domain.vo.common.PageVO;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.enums.MsgStatus;
import org.peter.chat.enums.exceptionStatus.ChatExceptionStatus;
import org.peter.chat.enums.exceptionStatus.UserExceptionStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.mapper.ChatHistoryMapper;
import org.peter.chat.mapper.qo.ChatHistoryMapperQO;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.service.app.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {
    @Autowired
    private Sid sid;

    @Autowired
    private ChatHistoryMapper chatHistoryMapper;

    @Autowired
    private UserService userService;

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

    @Override
    public PageVO<ChatHistoryVO> queryChatHistory(PageQO<ChatHistoryMapperQO> pageQO) {
        // mp分页插件的分页参数
        Page<ChatHistoryEntity> page = pageQO.transTo();
        page.setDesc("gmt_created");

        // 额外条件
        ChatHistoryMapperQO searchQO = pageQO.getCondition();

        // 判断是否为好友
        String myUserId = searchQO.getOneUserId();
        String friendUserId = searchQO.getOtherUserId();
        Boolean isFriend = userService.isHavingRelation(myUserId, friendUserId);
        if (!isFriend) {
            throw new BusinessException(UserExceptionStatus.NOT_FRIEND);
        }

        // 执行后page对象中会有结果集
        chatHistoryMapper.queryHistory(page, searchQO);

        return PageVO.transTo(page, ChatHistoryVO.class);
    }
}
