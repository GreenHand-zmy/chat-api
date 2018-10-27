package org.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.Packet;
import org.netty.packet.request.LoginRequestPacket;
import org.netty.packet.response.LoginResponsePacket;

@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        log.info("服务器接收到登录请求 packet={}", msg);
        Packet responsePacket = new LoginResponsePacket();

        // 登录校验
        if (valid(msg)) {
            ((LoginResponsePacket) responsePacket).setSuccess(true);
        } else {
            ((LoginResponsePacket) responsePacket).setSuccess(false)
                    .setMsg("用户名或密码错误");
        }
        ctx.channel().writeAndFlush(responsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
