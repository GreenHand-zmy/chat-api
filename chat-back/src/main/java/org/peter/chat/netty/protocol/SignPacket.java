package org.peter.chat.netty.protocol;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.netty.enums.PacketType;

import java.util.List;

@Data
@Accessors(chain = true)
public class SignPacket implements Packet {
    private List<String> messageIdList;

    @Override
    public Integer getType() {
        return PacketType.SIGN_PACKET.getTypeCode();
    }

    @Override
    public String getExtend() {
        return null;
    }
}
