package org.peter.chat.enums.exceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.peter.chat.enums.BaseStatus;

/**
 * 系统状态常量范围10001-10200
 */
@AllArgsConstructor
@Getter
public enum ChatExceptionStatus implements BaseStatus {
    AUTHENTICATION_EXCEPTION(10001, "认证失败"),
    UPLOAD_QRCODE_FAIL(10002, "上传二维码失败"),
    APP_RUNTIME_EXCEPTION(10003, "系统内部异常,请联系管理员");

    private int code;
    private String message;

    @Override
    public String getInfo() {
        return message;
    }
}
