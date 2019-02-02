package org.peter.chat.netty.channelHandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.peter.chat.netty.protocol.SignPacket;
import org.peter.chat.service.ServiceThreadPool;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.utils.SpringUtil;

import java.util.List;

@ChannelHandler.Sharable
public class SignMsgHandler extends SimpleChannelInboundHandler<SignPacket> {
    private final static ChatHistoryService chatHistoryService;
    private final static ServiceThreadPool THREAD_POOL;

    private static final SignMsgHandler INSTANCE = new SignMsgHandler();

    private SignMsgHandler() {
    }

    static {
        chatHistoryService = SpringUtil.getBean(ChatHistoryService.class);
        THREAD_POOL = SpringUtil.getBean(ServiceThreadPool.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SignPacket msg) throws Exception {
        // 执行签收
        List<String> messageIdList = msg.getMessageIdList();
        THREAD_POOL.submit(() -> chatHistoryService.batchSignMessage(messageIdList));
    }

    public static SignMsgHandler instance() {
        return INSTANCE;
    }
}
