package org.peter.chat.netty.channelHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.peter.chat.netty.SessionHolder;

@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private static final HeartBeatHandler INSTANCE = new HeartBeatHandler();

    private HeartBeatHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE: {
                    System.out.println("进入读空闲...");
                    break;
                }
                case WRITER_IDLE: {
                    System.out.println("进入写空闲...");
                    break;
                }
                case ALL_IDLE: {
                    System.out.println("进入读写空闲...");
                    // 关闭channel,并从映射关系中移除
                    Channel channel = ctx.channel();
                    channel.close();
                    SessionHolder.removeChannel(channel);
                    break;
                }
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    public static HeartBeatHandler instance(){
        return INSTANCE;
    }
}
