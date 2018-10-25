package org.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientBootStrap {
    private static final int MAX_RETRY = 10;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();


        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());

        bootstrap.connect("127.0.0.1", 8081)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("客户端连接成功");
                    } else {
                        log.info("客户端连接失败");
                        reConnection(bootstrap, "127.0.0.1", 8081, MAX_RETRY);
                    }
                });
    }

    private static void reConnection(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("客户端在第" + (MAX_RETRY - retry + 1) + "连接成功");
                    } else if (retry == 0) {
                        log.info("已重试最大次数");
                    } else {
                        reConnection(bootstrap, host, 8081, retry - 1);
                    }
                });
    }
}
