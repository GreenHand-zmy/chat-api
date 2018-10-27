package org.netty.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.PacketDecoder;
import org.netty.common.PacketEncoder;
import org.netty.common.Spliter;
import org.netty.common.StatisticsHandler;

@Slf4j
public class ServerInitializer extends ChannelInitializer<NioSocketChannel> {


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new StatisticsHandler())
                .addLast(new Spliter())
                .addLast(new PacketDecoder())
                .addLast(new LoginRequestHandler())
                .addLast(new AuthHandler())
                .addLast(new MessageRequestHandler())
                .addLast(new PacketEncoder());
    }
}
