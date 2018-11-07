package org.netty.protocol.request;

import org.netty.protocol.Packet;

public class JoinGroupRequestPacket implements Packet {
    @Override
    public byte getCommand() {
        return 0;
    }
}
