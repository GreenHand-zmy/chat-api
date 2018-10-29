package org.netty.protocol.response;

import lombok.Data;
import org.netty.protocol.Packet;
import org.netty.protocol.command.Command;

@Data
public class LogoutResponsePacket implements Packet {
    private boolean success;
    private String msg;

    @Override
    public byte getCommand() {
        return Command.LOGOUT_RESPONSE;
    }

}
