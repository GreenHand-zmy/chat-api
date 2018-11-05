package org.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;
import org.netty.protocol.request.CreateGroupRequestPacket;
import org.netty.protocol.response.CreateGroupResponsePacket;
import org.netty.util.IDUtil;
import org.netty.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) throws Exception {

        String groupOwnerId = msg.getOwnerId();

        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        List<String> memberUsernameList = new ArrayList<>();

        // 添加群主以及群友到一个channelGroup中
        channelGroup.add(SessionUtil.getChannel(groupOwnerId));
        memberUsernameList.add(SessionUtil.getSession(ctx.channel()).getUsername());
        for (String memberId : msg.getMemberIdList()) {
            Channel channel = SessionUtil.getChannel(memberId);
            if (channel != null) {
                channelGroup.add(channel);
                memberUsernameList.add(SessionUtil.getSession(channel).getUsername());
            }
        }

        // 发送响应
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setSuccess(true)
                .setGroupId(IDUtil.randomId())
                .setUsernameList(memberUsernameList);

        channelGroup.writeAndFlush(responsePacket);

        log.info("成功创建id={},size={}", responsePacket.getGroupId(),
                responsePacket.getUsernameList().size());
    }
}
