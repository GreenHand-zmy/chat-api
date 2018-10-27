package org.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.Packet;
import org.netty.packet.PacketCodeC;
import org.netty.packet.request.LoginRequestPacket;
import org.netty.packet.response.LoginResponsePacket;
import org.netty.packet.response.MessageResponsePacket;
import org.netty.util.SessionUtil;

@Slf4j
@Deprecated
public class ClientHandler extends ChannelInboundHandlerAdapter {
    // 当与服务器连接成功,发送登录数据包
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 创建数据包
        Packet loginPacket = new LoginRequestPacket();
        ((LoginRequestPacket) loginPacket).setUsername("zmy")
                .setPassword("admin");
        // 将数据包构造为byteBuf
        ByteBuf loginRequest = PacketCodeC.instance().encode(loginPacket);

        // 发送给服务器
        ctx.channel().writeAndFlush(loginRequest);
    }

    // 接受到服务器发送的响应
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf response = (ByteBuf) msg;
        Packet responsePacket = PacketCodeC.instance().decode(response);

        if (responsePacket instanceof LoginResponsePacket) {
            isLoginSuccess(ctx, (LoginResponsePacket) responsePacket);
        } else if (responsePacket instanceof MessageResponsePacket) {
            log.info("收到服务器数据: " + ((MessageResponsePacket) responsePacket).getMessage());
        }
    }

    private void isLoginSuccess(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            // 将channel标记为已登录
//            SessionUtil.markAsLogin(ctx.channel());
            log.info("登录成功");
        } else {
            log.info("登录失败,原因: " + loginResponsePacket.getMsg());
        }
    }
}
