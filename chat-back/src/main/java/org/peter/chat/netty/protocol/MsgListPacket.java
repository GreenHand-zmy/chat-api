package org.peter.chat.netty.protocol;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.netty.enums.PacketType;

import java.util.List;

/**
 * 消息集合包
 */
@Data
@Accessors(chain = true)
public class MsgListPacket implements Packet {
    private List<MsgPacket> msgPacketList;

    @Override
    public Integer getType() {
        return PacketType.NORMAL_MSG_LIST_PACKET.getTypeCode();
    }

    @Override
    public String getExtend() {
        return null;
    }


}
