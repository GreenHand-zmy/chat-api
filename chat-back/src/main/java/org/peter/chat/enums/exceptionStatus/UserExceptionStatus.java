package org.peter.chat.enums.exceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.peter.chat.enums.BaseStatus;

/**
 * 用户状态常量10201 - 10400
 */
@AllArgsConstructor
@Getter
public enum UserExceptionStatus implements BaseStatus {
    ACCOUNT_OR_PASSWORD_ERROR(10201, "账号或密码错误"),
    USERNAME_EXISTED_EXCEPTION(10202, "用户名已存在"),
    USER_NOT_EXISTED_EXCEPTION(10203, "不存在该用户"),
    INVALID_USER_ID(10204, "无效的用户编号"),
    ILLEGAL_USER_ACCESS(10205, "非法的用户请求");

    private int code;
    private String info;
}
