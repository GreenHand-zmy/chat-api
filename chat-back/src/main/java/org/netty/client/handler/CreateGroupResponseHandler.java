package org.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.protocol.response.CreateGroupResponsePacket;

/**
 * 处理创建群响应
 */
@Slf4j
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket msg) throws Exception {
        if (msg.isSuccess()) {
            log.info("您被拉入了id={},该群中有{}",
                    msg.getGroupId(), msg.getUsernameList());
        }
    }
}
