package org.peter.chat.web;

import org.peter.chat.domain.vo.common.UserCommonVO;
import org.springframework.core.NamedThreadLocal;

/**
 * 存储请求上下文
 */
public final class RequestContextHolder {
    private static final ThreadLocal<UserCommonVO> HOLDER = new NamedThreadLocal<>("userContext");

    public static void set(UserCommonVO userCommonVO) {
        HOLDER.set(userCommonVO);
    }

    public static UserCommonVO get() {
        return HOLDER.get();
    }

    public static void reset() {
        HOLDER.remove();
    }
}
