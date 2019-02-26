package org.peter.chat.enums.exceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.peter.chat.enums.BaseStatus;

/**
 * 好友请求状态常量10401 - 10600
 */
@AllArgsConstructor
@Getter
public enum FriendRequestExceptionStatus implements BaseStatus {
    REPEAT_ADD_USER_REQUEST(10401, "重复的好友请求"),
    INVALID_ADD_USER_REQUEST(10402, "无效的好友请求"),
    SOLVED_USER_REQUEST(10403, "已处理过的好友请求"),
    USER_REQUEST_NOT_EXISTED(10404, "不存在的好友请求"),
    ILLEGAL_SOLVE_USER_REQUEST(10405, "非法的好友请求处理,不能处理其他用户处理请求");

    private int code;
    private String info;

}
