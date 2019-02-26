package org.peter.chat.enums;

public enum MsgStatus {
    NOT_SIGN(0, "消息未签收"),
    SIGNED(1, "消息已签收");

    Integer code;
    String info;

    MsgStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
