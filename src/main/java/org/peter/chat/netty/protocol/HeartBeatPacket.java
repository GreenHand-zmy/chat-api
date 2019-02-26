package org.peter.chat.netty.protocol;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.netty.enums.PacketType;

/**
 * 心跳包,暂时不需要额外的心跳信息
 */
@Data
@Accessors(chain = true)
public class HeartBeatPacket implements Packet {

    @Override
    public Integer getType() {
        return PacketType.HEART_BEAT_PACKET.getTypeCode();
    }

    @Override
    public String getExtend() {
        return null;
    }
}
