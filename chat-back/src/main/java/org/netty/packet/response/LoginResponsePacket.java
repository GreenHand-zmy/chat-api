package org.netty.packet.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.command.Command;
import org.netty.packet.Packet;

@Data
@Accessors(chain = true)
public class LoginResponsePacket implements Packet {
    private boolean success;
    private String msg;

    @Override
    public byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
