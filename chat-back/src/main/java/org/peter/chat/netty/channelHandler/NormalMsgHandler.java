package org.peter.chat.netty.channelHandler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.netty.SessionHolder;
import org.peter.chat.netty.protocol.MsgPacket;
import org.peter.chat.service.ServiceThreadPool;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.utils.SpringUtil;

@ChannelHandler.Sharable
public class NormalMsgHandler extends SimpleChannelInboundHandler<MsgPacket> {
    private final static ChatHistoryService CHAT_HISTORY_SERVICE;
    private final static ServiceThreadPool THREAD_POOL;

    private static final NormalMsgHandler INSTANCE = new NormalMsgHandler();

    static {
        CHAT_HISTORY_SERVICE = SpringUtil.getBean(ChatHistoryService.class);
        THREAD_POOL = SpringUtil.getBean(ServiceThreadPool.class);
    }

    private NormalMsgHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgPacket msgPacket) throws Exception {
        // 保存到数据库中
        THREAD_POOL.submit(() -> {
            ChatHistoryEntity entity = new ChatHistoryEntity();
            entity.setSendUserId(msgPacket.getSenderId())
                    .setAcceptUserId(msgPacket.getReceiverId())
                    .setMsg(msgPacket.getMsg());
            CHAT_HISTORY_SERVICE.save(entity);

            // 设置保存到数据库中的消息编号
            msgPacket.setMsgId(entity.getId());

        });

        // 通过channel发送报文给接受者
        Channel receiverChannel = SessionHolder.getSession(msgPacket.getReceiverId());
        if (receiverChannel == null) {
            // TODO channel为空代表用户离线,推送消息
            System.out.println("TODO channel为空代表用户离线,推送消息");
        } else {
            // 将消息转发出去
            receiverChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgPacket)));
        }
    }

    public static NormalMsgHandler instance() {
        return INSTANCE;
    }
}
