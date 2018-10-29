package org.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.netty.client.console.ConsoleCommandManager;
import org.netty.client.console.LoginConsoleCommand;
import org.netty.client.handler.ClientInitializer;
import org.netty.protocol.request.LoginRequestPacket;
import org.netty.protocol.request.MessageRequestPacket;
import org.netty.util.SessionUtil;

import java.util.Scanner;

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
                        Channel channel = ((ChannelFuture) future).channel();
                        // 打开控制台输入
                        startConsoleThread(channel);
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
                        Channel channel = ((ChannelFuture) future).channel();
                        // 打开控制台输入
                        startConsoleThread(channel);
                    } else if (retry == 0) {
                        log.info("已重试最大次数");
                    } else {
                        reConnection(bootstrap, host, 8081, retry - 1);
                    }
                });
    }

    private static void startConsoleThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
                    loginConsoleCommand.exec(sc, channel);
                } else {
                    consoleCommandManager.exec(sc, channel);
                }
            }
        }).start();
    }
}
