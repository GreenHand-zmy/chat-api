package org.peter.chat.netty.channelHandler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.netty.SessionHolder;
import org.peter.chat.netty.protocol.ConnectPacket;
import org.peter.chat.netty.protocol.MsgListPacket;
import org.peter.chat.netty.protocol.MsgPacket;
import org.peter.chat.service.ServiceThreadPool;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

@ChannelHandler.Sharable
public class ConnectHandler extends SimpleChannelInboundHandler<ConnectPacket> {
    private final static ChatHistoryService chatHistoryService;
    private final static ServiceThreadPool THREAD_POOL;

    // 构造单例
    private static final ConnectHandler INSTANCE = new ConnectHandler();

    static {
        chatHistoryService = SpringUtil.getBean(ChatHistoryService.class);
        THREAD_POOL = SpringUtil.getBean(ServiceThreadPool.class);
    }

    private ConnectHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectPacket msg) throws Exception {
        // 放入SessionHolder中
        String userId = msg.getUserId();
        SessionHolder.putSession(userId, ctx.channel());

        THREAD_POOL.submit(() -> {
            // 判断用户是否有未读信息,如果有发送给客户端
            if (chatHistoryService.countUnReadMsg(userId) > 0) {
                List<ChatHistoryEntity> chatHistoryEntityList = chatHistoryService.queryUnReadMsgByUserId(userId);
                List<MsgPacket> msgPacketList = chatHistoryEntityList
                        .stream()
                        .map(MsgPacket::transTo)
                        .collect(Collectors.toList());

                MsgListPacket msgListPacket = new MsgListPacket();
                msgListPacket.setMsgPacketList(msgPacketList);
                // 将消息转发出去
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgListPacket)));
            }
        });
    }

    public static ConnectHandler instance() {
        return INSTANCE;
    }
}
