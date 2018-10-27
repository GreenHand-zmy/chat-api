package org.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.Packet;
import org.netty.packet.request.LoginRequestPacket;
import org.netty.packet.response.LoginResponsePacket;
import org.netty.util.LoginUtil;

@Slf4j
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 创建数据包
        Packet loginPacket = new LoginRequestPacket();
        ((LoginRequestPacket) loginPacket).setUserId("sjdasdxasd")
                .setUsername("zmy")
                .setPassword("admin");

        ctx.channel().writeAndFlush(loginPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket) throws Exception {
        isLoginSuccess(ctx, responsePacket);
    }

    private void isLoginSuccess(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            // 将channel标记为已登录
            LoginUtil.markAsLogin(ctx.channel());
            log.info("登录成功");
        } else {
            log.info("登录失败,原因: " + loginResponsePacket.getMsg());
        }
    }
}
