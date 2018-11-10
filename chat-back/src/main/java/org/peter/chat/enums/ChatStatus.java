package org.peter.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatStatus {
    AUTHENTICATION_EXCEPTION(10000, "认证失败"),
    ACCOUNT_OR_PASSWORD_ERROR(10001, "账号或密码错误"),
    USERNAME_EXISTED_EXCEPTION(10002, "用户名已存在"),
    INVALID_USER_ID(10003, "无效的用户编号"),
    UPLOAD_QRCODE_FAIL(10005, "上传二维码失败"),
    APP_RUNTIME_EXCEPTION(10004, "系统内部异常,请联系管理员");

    private int code;
    private String message;

}
