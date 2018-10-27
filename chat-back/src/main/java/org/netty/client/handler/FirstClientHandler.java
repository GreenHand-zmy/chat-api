package org.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Date;

@Slf4j
@Deprecated
public class FirstClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ByteBuf byteBuf = getByteBuf(ctx);
                ctx.writeAndFlush(byteBuf);
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf response = (ByteBuf) msg;
        log.info("客户端收到服务器响应" + response.toString(Charset.forName("UTF-8")));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1.获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2.准备数据,指定字符串的字符串 utf-8
        byte[] bytes = ("客户端发送" + new Date()).getBytes(Charset.forName("UTF-8"));

        // 填充数据到bytebuf
        buffer.writeBytes(bytes);

        return buffer;
    }
}
