package org.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.PacketCodeC;
import org.netty.packet.request.MessageRequestPacket;
import org.netty.packet.response.MessageResponsePacket;

import java.util.Date;

@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket requestPacket) throws Exception {
        // 处理消息
        log.info(new Date() + "服务器收到数据 " + requestPacket.getMessage());

        // 响应消息
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMessage(new Date() + "您好");

        ctx.channel().writeAndFlush(responsePacket);
    }
}
