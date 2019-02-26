package org.peter.chat.netty.enums;

public enum PacketType {
    // 1-100预留给系统相关的包
    CONNECT_PACKET(1, "连接包"),
    HEART_BEAT_PACKET(2,"心跳包"),
    // 101-200预留给消息类型的包
    NORMAL_MSG_PACKET(101, "普通的定向聊天包"),
    NORMAL_MSG_LIST_PACKET(102, "普通的定向聊天包集合"),
    // 201-300预留给响应包
    SIGN_PACKET(201, "消息签收包");


    int typeCode;
    String typeInfo;

    PacketType(int typeCode, String typeInfo) {
        this.typeCode = typeCode;
        this.typeInfo = typeInfo;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeInfo() {
        return typeInfo;
    }
}
