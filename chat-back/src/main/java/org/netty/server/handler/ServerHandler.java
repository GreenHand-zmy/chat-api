package org.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.request.LoginRequestPacket;
import org.netty.packet.Packet;
import org.netty.packet.PacketCodeC;
import org.netty.packet.request.MessageRequestPacket;
import org.netty.packet.response.LoginResponsePacket;
import org.netty.packet.response.MessageResponsePacket;

import java.util.Date;

@Slf4j
@Deprecated
public class ServerHandler extends ChannelInboundHandlerAdapter {

    // 当客户端将登录请求发到服务器时
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;

        // 将byteBuf解析为数据包
        Packet requestPacket = PacketCodeC.instance().decode(request);

        if (requestPacket instanceof LoginRequestPacket) {
            log.info("服务器接收到登录请求 packet={}", requestPacket);
            Packet responsePacket = new LoginResponsePacket();

            // 登录校验
            if (valid((LoginRequestPacket) requestPacket)) {
                ((LoginResponsePacket) responsePacket).setSuccess(true);
            } else {
                ((LoginResponsePacket) responsePacket).setSuccess(false)
                        .setMsg("用户名或密码错误");
            }
            // 将数据包编码
            ByteBuf response = PacketCodeC.instance().encode(responsePacket);
            ctx.channel().writeAndFlush(response);
        } else if (requestPacket instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket packet = (MessageRequestPacket) requestPacket;
            log.info(new Date() + "服务器收到数据 " + packet.getMessage());

            // 响应消息
            MessageResponsePacket responsePacket = new MessageResponsePacket();
            responsePacket.setMessage(new Date() + "您好");
            ByteBuf byteBuf = PacketCodeC.instance().encode(responsePacket);
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
