package org.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.protocol.request.NotifyResponsePacket;

@Slf4j
public class NotifyResponseHandler extends SimpleChannelInboundHandler<NotifyResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NotifyResponsePacket msg) throws Exception {
        log.info("服务器发来提醒: " + msg.getNotifyString());
    }
}
