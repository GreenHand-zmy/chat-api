package org.peter.chat.netty.channelHandler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.peter.chat.domain.vo.UnReadMsgVO;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.netty.SessionHolder;
import org.peter.chat.netty.enums.PacketType;
import org.peter.chat.netty.protocol.*;
import org.peter.chat.service.app.ChatHistoryService;
import org.peter.chat.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理消息的handler
 * TextWebSocketFrame：在netty中,是用于为websocket专门处理文本的对象,frame是消息的载体
 */
@Deprecated
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //  用于记录和管理所有客户端的channel
    private static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ChatHistoryService chatHistoryService;

    static {
        chatHistoryService = SpringUtil.getBean(ChatHistoryService.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接受到的数据:" + content);

        // 解析报文
        Packet packet = JSON.parseObject(content, AbstractPacket.class);

        // 如果为连接报文,则放入SessionHolder中
        if (PacketType.CONNECT_PACKET.getTypeCode() == packet.getType()) {
            ConnectPacket connectPacket = JSON.parseObject(content, ConnectPacket.class);
            String userId = connectPacket.getUserId();
            SessionHolder.putSession(userId, ctx.channel());

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
        } else if (PacketType.NORMAL_MSG_PACKET.getTypeCode() == packet.getType()) {
            // 如果为普通的聊天信息
            MsgPacket msgPacket = JSON.parseObject(content, MsgPacket.class);

            // 保存到数据库中
            ChatHistoryEntity entity = new ChatHistoryEntity();
            entity.setSendUserId(msgPacket.getSenderId())
                    .setAcceptUserId(msgPacket.getReceiverId())
                    .setMsg(msgPacket.getMsg());
            chatHistoryService.save(entity);

            // 设置保存到数据库中的消息编号
            msgPacket.setMsgId(entity.getId());

            // 通过channel发送报文给接受者
            Channel receiverChannel = SessionHolder.getSession(msgPacket.getReceiverId());
            if (receiverChannel == null) {
                // TODO channel为空代表用户离线,推送消息
                System.out.println("TODO channel为空代表用户离线,推送消息");
            } else {
                // 将消息转发出去
                receiverChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgPacket)));
            }

        } else if (PacketType.SIGN_PACKET.getTypeCode() == packet.getType()) {
            // 如果为签收包,执行签收
            SignPacket signPacket = JSON.parseObject(content, SignPacket.class);
            List<String> messageIdList = signPacket.getMessageIdList();
            chatHistoryService.batchSignMessage(messageIdList);
        } else if (PacketType.HEART_BEAT_PACKET.getTypeCode() == packet.getType()) {
            HeartBeatPacket heartBeatPacket = JSON.parseObject(content, HeartBeatPacket.class);
            System.out.println("收到客户端: " + ctx.channel().id().asShortText() + "发送的心跳包 " + heartBeatPacket);
        }
    }

    /**
     * 当客户端连接服务端之后(打开连接)
     * 获取客户端的channel,并且放到ChannelGroup中进行管理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    // 用户离开(断开连接) 例如关闭浏览器
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved,ChannelGroup会自动移除对应客户端的channel
        clients.remove(ctx.channel());

        SessionHolder.removeChannel(ctx.channel());
        System.out.println("客户端断开,channel对应的长id为:" + ctx.channel().id().asLongText());
    }
}
