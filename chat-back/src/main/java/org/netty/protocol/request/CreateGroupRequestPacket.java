package org.netty.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.netty.protocol.Packet;
import org.netty.protocol.command.Command;

import java.util.List;

@Data
@Accessors(chain = true)
public class CreateGroupRequestPacket implements Packet {
    // 群主编号
    private String ownerId;

    // 群成员编号集合
    private List<String> memberIdList;

    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
