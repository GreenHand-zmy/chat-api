package org.peter.chat.web;

import org.peter.chat.domain.vo.UserVo;
import org.springframework.core.NamedThreadLocal;

/**
 * 存储请求上下文
 */
public final class RequestContextHolder {
    private static final ThreadLocal<UserVo> HOLDER = new NamedThreadLocal<>("userContext");

    public static void set(UserVo userVo) {
        HOLDER.set(userVo);
    }

    public static UserVo get() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.remove();
    }
}
