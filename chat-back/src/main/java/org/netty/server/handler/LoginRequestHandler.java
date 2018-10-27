package org.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.Session;
import org.netty.domain.vo.UserVo;
import org.netty.packet.Packet;
import org.netty.packet.request.LoginRequestPacket;
import org.netty.packet.response.LoginResponsePacket;
import org.netty.util.SessionUtil;
import org.springframework.beans.BeanUtils;

import java.util.Random;
import java.util.UUID;

@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        log.info("服务器处理登录请求 packet={}", loginRequestPacket);
        LoginResponsePacket responsePacket = new LoginResponsePacket();

        // 登录校验
        if (valid(loginRequestPacket)) {
            responsePacket.setSuccess(true);

            // 服务端生成随机用户编号
            String userId = randomUserId();
            String username = loginRequestPacket.getUsername();

            // 生成vo对象
            UserVo userVo = new UserVo();
            userVo.setUserId(userId)
                    .setUsername(username);
            responsePacket.setUserVo(userVo);

            // 生成session对象,并绑定到channel上
            Session session = new Session();
            session.setUserId(userId)
                    .setUsername(userVo.getUsername());
            SessionUtil.bindSession(session, ctx.channel());
        } else {
            responsePacket.setSuccess(false)
                    .setMsg("用户名或密码错误");
        }
        ctx.channel().writeAndFlush(responsePacket);
    }

    // 用户断线后需要取消绑定
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
        super.channelInactive(ctx);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    private String randomUserId() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("a")
                    .append(random.nextInt(26));
        }
        return sb.toString();
    }
}
