package org.netty.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.protocol.command.Command;
import org.netty.protocol.Packet;

@Data
@Accessors(chain = true)
public class LoginRequestPacket implements Packet {
    private String username;
    private String password;

    @Override
    public byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
