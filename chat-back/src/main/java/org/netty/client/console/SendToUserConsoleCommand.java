package org.netty.client.console;

import io.netty.channel.Channel;
import org.netty.protocol.request.MessageRequestPacket;

import java.util.Scanner;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入收到信息的用户编号");
        String toUserId = scanner.next();
        System.out.println("请输入消息内容");
        String text = scanner.next();

        MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
        messageRequestPacket.setToUserId(toUserId)
                .setMessage(text);

        channel.writeAndFlush(messageRequestPacket);
    }
}
