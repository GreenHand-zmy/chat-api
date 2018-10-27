package org.netty.packet.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.command.Command;
import org.netty.packet.Packet;

@Data
@Accessors(chain = true)
public class LoginRequestPacket implements Packet {
    private String userId;
    private String username;
    private String password;

    @Override
    public byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
