package org.netty.protocol.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.protocol.command.Command;
import org.netty.protocol.Packet;

@Data
@Accessors(chain = true)
public class MessageResponsePacket implements Packet {
    private String fromUserId;
    private String fromUsername;
    private String message;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}