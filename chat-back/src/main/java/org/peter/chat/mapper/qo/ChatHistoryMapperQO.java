package org.peter.chat.mapper.qo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ChatHistoryMapperQO {
    private String oneUserId;
    private String otherUserId;
    private Date chatDate;
}
