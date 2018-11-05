package org.netty.protocol.request;

import lombok.Data;
import org.netty.protocol.Packet;
import org.netty.protocol.command.Command;

@Data
public class NotifyResponsePacket implements Packet {
    private String notifyString;

    @Override
    public byte getCommand() {
        return Command.NOTIFY_RESPONSE;
    }
}
