package org.netty.packet.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.command.Command;
import org.netty.packet.Packet;

@Data
@Accessors(chain = true)
public class MessageRequestPacket implements Packet {
    // 接受信息的用户编号
    private String toUserId;
    // 信息
    private String message;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
