package org.netty.packet.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.command.Command;
import org.netty.packet.Packet;

@Data
@Accessors(chain = true)
public class MessageResponsePacket implements Packet {
    private String message;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
