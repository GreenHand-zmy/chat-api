package org.netty.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.netty.codec.PacketDecoder;
import org.netty.codec.PacketEncoder;
import org.netty.codec.Spliter;
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
                .addLast(new CreateGroupRequestHandler())
                .addLast(new LogoutRequestHandler())
                .addLast(new PacketEncoder());
    }
}
