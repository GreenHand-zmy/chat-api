package org.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
@Deprecated
public class FirstServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;

        log.info("服务器读到数据 ->" + request.toString(Charset.forName("utf-8")));

        ByteBuf response = getResponse(ctx, request);
        ctx.channel().writeAndFlush(response);
    }

    private ByteBuf getResponse(ChannelHandlerContext ctx, ByteBuf request) {
        ByteBuf response = ctx.alloc().buffer();

        String data = "服务器响应: " + request.toString(Charset.forName("utf-8"));

        response.writeBytes(data.getBytes());

        return response;
    }
}
