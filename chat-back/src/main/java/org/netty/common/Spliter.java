package org.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.netty.packet.Packet;
import org.netty.packet.PacketCodeC;

/**
 * 拆包器
 */
@Slf4j
public class Spliter extends LengthFieldBasedFrameDecoder {
    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 屏蔽非本协议的连接
        if (in.getInt(in.readerIndex()) != Packet.MAGIC_NUMBER) {
            log.info(ctx.channel().id().asLongText() + "传输了非本协议");
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
