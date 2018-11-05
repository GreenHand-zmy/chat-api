package org.netty.client.console;

import io.netty.channel.Channel;
import org.netty.protocol.request.CreateGroupRequestPacket;
import org.netty.util.SessionUtil;

import java.util.Arrays;
import java.util.Scanner;

public class CreateGroupConsoleCommand implements ConsoleCommand {
    public static final String USER_ID_SPLITER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("【拉人群聊】输入 userId 列表，userId 之间英文逗号隔开：");
        String userIds = scanner.next();
        CreateGroupRequestPacket requestPacket = new CreateGroupRequestPacket();
        requestPacket.setOwnerId(SessionUtil.getSession(channel).getUserId())
                .setMemberIdList(Arrays.asList(userIds.split(USER_ID_SPLITER)));

        channel.writeAndFlush(requestPacket);
    }
}
