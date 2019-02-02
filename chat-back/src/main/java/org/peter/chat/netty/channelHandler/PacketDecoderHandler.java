package org.peter.chat.netty.channelHandler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.peter.chat.netty.enums.PacketType;
import org.peter.chat.netty.protocol.*;

/**
 * 解码传递过来的json格式的指令
 */
public class PacketDecoderHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        System.out.println("接受到的数据:" + content);

        // 解析报文
        Packet packet = JSON.parseObject(content, AbstractPacket.class);

        if (PacketType.CONNECT_PACKET.getTypeCode() == packet.getType()) {
            // 如果为连接报文
            ConnectPacket connectPacket = JSON.parseObject(content, ConnectPacket.class);
            ctx.pipeline().fireChannelRead(connectPacket);
        } else if (PacketType.NORMAL_MSG_PACKET.getTypeCode() == packet.getType()) {
            // 如果为普通的聊天信息
            MsgPacket msgPacket = JSON.parseObject(content, MsgPacket.class);
            ctx.pipeline().fireChannelRead(msgPacket);
        } else if (PacketType.SIGN_PACKET.getTypeCode() == packet.getType()) {
            // 如果为签收包
            SignPacket signPacket = JSON.parseObject(content, SignPacket.class);
            ctx.pipeline().fireChannelRead(signPacket);
        } else if (PacketType.HEART_BEAT_PACKET.getTypeCode() == packet.getType()) {
            // 如果为心跳包
            HeartBeatPacket heartBeatPacket = JSON.parseObject(content, HeartBeatPacket.class);
            ctx.pipeline().fireChannelRead(heartBeatPacket);
        }
    }
}
