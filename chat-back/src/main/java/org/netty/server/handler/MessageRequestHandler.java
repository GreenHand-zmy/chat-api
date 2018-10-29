package org.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.Session;
import org.netty.protocol.request.MessageRequestPacket;
import org.netty.protocol.response.MessageResponsePacket;
import org.netty.util.SessionUtil;

@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket requestPacket) throws Exception {
        // 处理消息
        log.info("服务器收到数据 " + requestPacket.getMessage());

        // 拿到发送方的session信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 响应消息
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setFromUserId(session.getUserId())
                .setFromUsername(session.getUsername())
                .setMessage(requestPacket.getMessage());

        // 拿到接收方的channel
        Channel toUserChannel = SessionUtil.getChannel(requestPacket.getToUserId());

        // 将信息发给接受方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(responsePacket);
        } else {
            responsePacket = new MessageResponsePacket();
            responsePacket.setMessage("对方不在线,发送失败");
            ctx.channel().writeAndFlush(responsePacket);
        }
    }
}
