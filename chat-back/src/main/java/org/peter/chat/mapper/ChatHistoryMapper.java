package org.peter.chat.mapper;

import org.peter.chat.entity.ChatHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
