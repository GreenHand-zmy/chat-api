package org.peter.chat.web;

import org.peter.chat.domain.vo.UserVoWithoutToken;
import org.springframework.core.NamedThreadLocal;

/**
 * 存储请求上下文
 */
public final class RequestContextHolder {
    private static final ThreadLocal<UserVoWithoutToken> HOLDER = new NamedThreadLocal<>("userContext");

    public static void set(UserVoWithoutToken userVoWithoutToken) {
        HOLDER.set(userVoWithoutToken);
    }

    public static UserVoWithoutToken get() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.remove();
    }
}
