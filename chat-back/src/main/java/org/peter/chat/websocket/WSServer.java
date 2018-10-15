package org.peter.chat.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;

/**
 * websocket服务器
 */
public class WSServer {
    // ws端口号
    @Value("${chat.webSocket.port}")
    private int wsPort;

    public static void main(String[] args) throws InterruptedException {
        // 定义一对线程组
        // 主线程组,用于接受客户端的连接,但是不做任何处理,跟老板一样,不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 从线程组,老板线程组会把任务丢给他,让手下线程组做任务
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // netty服务器的创建,ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitializer());

            // 启动server,并且设置8088为启动的端口号,同时启动为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

            // 监听关闭的channel,设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
