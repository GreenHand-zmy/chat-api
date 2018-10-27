package org.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.math.BigDecimal;

/**
 * 统计流量
 */
public class StatisticsHandler extends ChannelInboundHandlerAdapter {
    private static BigDecimal comingBytes = new BigDecimal(0);
    private static BigDecimal clientCount = new BigDecimal(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        comingBytes = comingBytes.add(new BigDecimal(data.readableBytes()));

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端连接数加1
        clientCount = clientCount.add(new BigDecimal(1));
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开");
        clientCount = clientCount.subtract(new BigDecimal(1));
        super.channelInactive(ctx);
    }

    public static BigDecimal getComingBytes() {
        return comingBytes;
    }

    public static BigDecimal getClientCount() {
        return clientCount;
    }
}
