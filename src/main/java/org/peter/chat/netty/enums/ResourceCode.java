package org.peter.chat.netty.enums;

import lombok.Getter;

@Getter
public enum ResourceCode {
    FRIEND_RESOURCE(1, "好友列表资源"),FRIEND_REQUEST_LIST(2,"好友请求");

    int resourceCode;
    String resourceInfo;

    ResourceCode(int resourceCode, String resourceInfo) {
        this.resourceCode = resourceCode;
        this.resourceInfo = resourceInfo;
    }

    public int getResourceCode() {
        return resourceCode;
    }

}
