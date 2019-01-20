package org.peter.chat.netty.protocol;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.netty.enums.PacketType;

/**
 * 连接报文
 */
@Data
@Accessors(chain = true)
public class ConnectPacket implements Packet {
    private String userId;


    @Override
    public Integer getType() {
        return PacketType.CONNECT_PACKET.getTypeCode();
    }

    @Override
    public String getExtend() {
        return null;
    }
}
