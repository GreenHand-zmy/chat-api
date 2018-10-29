package org.netty.protocol.request;

import lombok.Data;
import org.netty.protocol.Packet;
import org.netty.protocol.command.Command;

@Data
public class LogoutRequestPacket implements Packet {

    @Override
    public byte getCommand() {
        return Command.LOGOUT_REQUEST;
    }
}
