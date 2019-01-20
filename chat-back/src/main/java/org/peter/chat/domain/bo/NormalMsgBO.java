package org.peter.chat.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NormalMsgBO {
    // 发送者编号
    private String senderId;
    // 接受者编号
    private String receiverId;
    // 信息内容
    private String msg;
}
