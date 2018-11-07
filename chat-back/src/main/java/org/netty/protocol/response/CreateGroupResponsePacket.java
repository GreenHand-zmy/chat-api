package org.netty.protocol.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.protocol.Packet;
import org.netty.protocol.command.Command;

import java.util.List;

@Data
@Accessors(chain = true)
public class CreateGroupResponsePacket implements Packet {
    private boolean success;
    private String groupId;
    private List<String> usernameList;

    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
