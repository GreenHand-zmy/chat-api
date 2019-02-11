package org.peter.chat.domain.qo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class HistorySearchQO implements Serializable {
    /**
     * 好友id
     */
    private String friendUserId;

    /**
     * 聊天日期
     */
    private Date chatDate;
}
