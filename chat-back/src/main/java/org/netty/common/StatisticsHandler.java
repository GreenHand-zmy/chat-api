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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        comingBytes = comingBytes.add(new BigDecimal(data.readableBytes()));

        super.channelRead(ctx, msg);
    }

    public static BigDecimal getComingBytes() {
        return comingBytes;
    }
}
