package org.peter.chat.netty.protocol;

import lombok.Data;

@Data
public class AbstractPacket implements Packet {
    private Integer type;
    private String extend;

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public String getExtend() {
        return extend;
    }
}
