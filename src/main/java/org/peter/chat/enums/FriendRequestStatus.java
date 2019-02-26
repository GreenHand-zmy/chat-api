package org.peter.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 好友请求状态 已接受 未接受 拒绝
 */
@AllArgsConstructor
@Getter
public enum FriendRequestStatus implements BaseStatus {
    NOT_DISPOSE(0, "好友请求已收到,但未处理"),
    ACCEPT(1, "好友请求已收到,并已接受"),
    REJECT(2, "好友请求已收到,并被拒绝");
    private int code;
    private String info;

}
