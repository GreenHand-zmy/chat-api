package org.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.Session;
import org.netty.domain.vo.UserVo;
import org.netty.packet.Packet;
import org.netty.packet.request.LoginRequestPacket;
import org.netty.packet.response.LoginResponsePacket;
import org.netty.util.SessionUtil;

@Slf4j
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket) throws Exception {
        isLoginSuccess(ctx, responsePacket);
    }

    private void isLoginSuccess(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            UserVo userVo = loginResponsePacket.getUserVo();

            // 将channel标记为已登录
            Session session = new Session();
            session.setUserId(userVo.getUserId())
                    .setUsername(userVo.getUsername());

            SessionUtil.bindSession(session, ctx.channel());
            log.info(userVo.getUsername() + " 登录成功");
        } else {
            log.info("登录失败,原因: " + loginResponsePacket.getMsg());
        }
    }
}
