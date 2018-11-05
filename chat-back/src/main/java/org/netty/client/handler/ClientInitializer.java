package org.netty.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.netty.codec.PacketDecoder;
import org.netty.codec.PacketEncoder;
import org.netty.codec.Spliter;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new Spliter())
                .addLast(new PacketDecoder())
                .addLast(new LoginResponseHandler())
                .addLast(new MessageResponseHandler())
                .addLast(new CreateGroupResponseHandler())
                .addLast(new NotifyResponseHandler())
                .addLast(new LogoutResponseHandler())
                .addLast(new PacketEncoder());
    }
}
