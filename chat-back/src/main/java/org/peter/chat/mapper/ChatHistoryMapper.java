package org.peter.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.mapper.qo.ChatHistoryMapperQO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zmy
 * @since 2018-10-14
 */
public interface ChatHistoryMapper extends BaseMapper<ChatHistoryEntity> {
    int batchUpdateHistorySign(List<String> idList);

    IPage<ChatHistoryEntity> queryHistory(Page page, @Param("chatHistoryMapperQO") ChatHistoryMapperQO chatHistoryMapperQO);
}
