package org.peter.chat.netty.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class PacketTest {
    @Test
    public void test() {
        MsgPacket msgPacket = new MsgPacket();
        msgPacket.setSenderId("xxx")
                .setReceiverId("yyy");

        String content = JSON.toJSONString(msgPacket);
        System.out.println(content);
        AbstractPacket packet = JSON.parseObject(content, AbstractPacket.class);

        System.out.println(packet);
    }
}