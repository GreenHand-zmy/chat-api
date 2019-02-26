package org.peter.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * websocket服务器
 */
@Component
public class WSServer {
    private Logger logger = LoggerFactory.getLogger(WSServer.class);

    // ws端口号
    @Value("${chat.webSocket.port}")
    private int wsPort = 8090;

    // 定义一对线程组
    // 主线程组,用于接受客户端的连接,但是不做任何处理,跟老板一样,不做事
    private EventLoopGroup bossGroup;

    // 从线程组,老板线程组会把任务丢给他,让手下线程组做任务
    private EventLoopGroup workGroup;

    // netty服务器的创建,ServerBootstrap是一个启动类
    private ServerBootstrap serverBootstrap;

    private static volatile WSServer instance;

    private WSServer() {
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();

        // netty服务器的创建,ServerBootstrap是一个启动类
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }

    public static WSServer getInstance() {
        if (instance == null) {
            synchronized (WSServer.class) {
                if (instance == null) {
                    instance = new WSServer();
                }
            }
        }
        return instance;
    }

    public void start() {
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(wsPort);
            showStatistics();
            logger.info("netty websocket server绑定在 " + wsPort + " 启动完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showStatistics() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    SessionHolder.showSession();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
