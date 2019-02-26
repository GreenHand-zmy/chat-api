package org.peter.chat.domain.bo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.peter.chat.enums.BaseStatus;

@AllArgsConstructor
@Getter
public enum FriendRequestType implements BaseStatus {
    ACCEPT(1, "好友请求接受类型"),
    REJECT(2, "好友请求拒绝类型");
    private int code;
    private String info;
}
