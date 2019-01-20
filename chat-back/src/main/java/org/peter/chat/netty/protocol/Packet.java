package org.peter.chat.netty.protocol;

import java.io.Serializable;

public interface Packet extends Serializable {
    // 消息包类型
    Integer getType();

    // 消息包额外内容
    String getExtend();
}
