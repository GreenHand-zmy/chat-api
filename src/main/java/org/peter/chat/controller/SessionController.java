package org.peter.chat.controller;

import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.web.RequestContextHolder;

/**
 * 获取用户会话信息
 */
public class SessionController {
    /**
     * 获取当前用户的会话信息
     *
     * @return
     */
    public static UserCommonVO getCurrentUserSession(){
        return RequestContextHolder.get();
    }
}
