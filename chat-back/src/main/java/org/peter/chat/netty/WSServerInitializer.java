package org.peter.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.peter.chat.netty.channelHandler.*;
import org.springframework.beans.factory.annotation.Value;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Value("${chat.webSocket.websocketPath}")
    private String websocketPath = "/ws";

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // websocket 基于http协议 所有要有http编解码器
        pipeline.addLast(new HttpServerCodec());

        // 添加写大数据流支持
        pipeline.addLast(new ChunkedWriteHandler());

        // 对httpMessage进行聚合,聚合成FullHttpRequest和FullHttpResponse
        // 几乎在netty中的编程,都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // ========================以上用于支持http协议==========================

        /**
         * websocket 服务器处理的协议,用于指定给客户端连接访问的路由
         * 本handler会帮你处理一些繁重的复杂的事
         * 会帮你处理握手动作：handshaking(close,ping,pong) ping + pong = 心跳
         * 对于websocket来讲,都是以frames进行传输的,不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));

        // 添加心跳支持
        pipeline.addLast(new IdleStateHandler(2, 4, 60));
        pipeline.addLast(HeartBeatHandler.instance());

        // 自定义handler,处理客户端的请求
//        pipeline.addLast(new ChatHandler());
        pipeline.addLast(PacketDecoderHandler.instance())
                .addLast(ConnectHandler.instance())
                .addLast(NormalMsgHandler.instance())
                .addLast(SignMsgHandler.instance());


    }
}
