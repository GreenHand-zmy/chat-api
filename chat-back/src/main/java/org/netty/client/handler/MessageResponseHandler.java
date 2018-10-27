package org.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.response.MessageResponsePacket;

@Slf4j
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket responsePacket) throws Exception {
        String fromUserId = responsePacket.getFromUserId();
        String fromUserName = responsePacket.getFromUsername();

        log.info(fromUserId + ":" + fromUserName + " -> " + responsePacket .getMessage());
    }
}
