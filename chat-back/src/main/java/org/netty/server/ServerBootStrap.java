package org.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.StatisticsHandler;
import org.netty.server.handler.ServerInitializer;

import java.math.BigDecimal;

@Slf4j
public class ServerBootStrap {
    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup(2);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)           // 设置线程模型
                .channel(NioServerSocketChannel.class)  // 设置io模型
                .childHandler(new ServerInitializer()); // 服务器初始化

        bind(bootstrap, 8081);

        showStatistics();
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("服务器成功在 port={}启动", port);
                    } else {
                        bind(serverBootstrap, port + 1);
                    }
                });
    }

    private static void showStatistics() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);
                    log.info("服务器当前流量为 " + StatisticsHandler.getComingBytes()
                            .divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP) + "kb");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
