package org.netty.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.protocol.command.Command;
import org.netty.protocol.Packet;

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
