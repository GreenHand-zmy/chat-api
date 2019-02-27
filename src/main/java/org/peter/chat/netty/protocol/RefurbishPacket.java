package org.peter.chat.netty.protocol;

import lombok.Data;
import org.peter.chat.netty.enums.PacketType;

/**
 * 刷新资源包
 * 存在该包的目的为,当服务端数据更新发送给客户端,让它拉取资源
 */
@Data
public class RefurbishPacket implements Packet {
    /**
     * 资源的编码
     */
    private Integer resourceCode;

    public RefurbishPacket(Integer resourceCode) {
        this.resourceCode = resourceCode;
    }

    @Override
    public Integer getType() {
        return PacketType.REFURBISH_PACKET.getTypeCode();
    }

    @Override
    public String getExtend() {
        return null;
    }
}
