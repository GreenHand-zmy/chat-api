package org.peter.chat.domain.vo.common;

import lombok.Data;

@Data
public class FriendRequestVO {
    //请求编号
    private String requestId;

    // 发送方编号
    private String senderId;

    // 发送方用户名
    private String senderUsername;

    // 发送方头像地址
    private String senderFaceImage;

    // 发送方大头像地址
    private String senderFaceImageBig;

    // 发送方昵称
    private String senderNickname;

    // 发送方二维码
    private String senderQrcode;

    // 发送方设备id
    private String senderClientId;
}
