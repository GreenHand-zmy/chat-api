package org.peter.chat.netty.protocol;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.domain.vo.UnReadMsgVO;
import org.peter.chat.entity.ChatHistoryEntity;
import org.peter.chat.netty.enums.PacketType;

/**
 * 普通的定向聊天信息包
 */
@Data
@Accessors(chain = true)
public class MsgPacket implements Packet {
    private String senderId;
    private String receiverId;
    private String msg;
    private String msgId;

    @Override
    public Integer getType() {
        return PacketType.NORMAL_MSG_PACKET.getTypeCode();
    }

    @Override
    public String getExtend() {
        return null;
    }

    public static MsgPacket transTo(ChatHistoryEntity chatHistoryEntity) {
        MsgPacket msgPacket = new MsgPacket();

        msgPacket.setMsgId(chatHistoryEntity.getId())
                .setMsg(chatHistoryEntity.getMsg())
                .setReceiverId(chatHistoryEntity.getAcceptUserId())
                .setSenderId(chatHistoryEntity.getSendUserId());
        return msgPacket;
    }
}
