package org.peter.chat.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.peter.chat.domain.vo.UserVoWithoutToken;
import org.peter.chat.enums.ChatStatus;
import org.peter.chat.enums.CustomHttpHeader;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.service.UserService;
import org.peter.chat.web.RequestContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器执行顺序
 * preHandle -> controller -> postHandle -> afterCompletion
 *
 * 登陆拦截器
 * 首先从httpHeader中获取自定义响应头,如果存在则
 */
@Component
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String remoteAddr = request.getRemoteAddr();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        // 获取自定义响应头
        String userToken = request.getHeader(CustomHttpHeader.X_USER_TOKEN.getHeaderName());
        log.info("remoteAddr={}, method={}, requestURI={}, userToken={}",
                remoteAddr, method, requestURI, userToken);

        if (userToken == null) {
            throw new BusinessException(ChatStatus.AUTHENTICATION_EXCEPTION,
                    "remoteAddr={}, method={}, requestURI={}",
                    remoteAddr, method, requestURI);
        }

        // 根据userToken查询是否存在该用户
        UserVoWithoutToken userVoWithoutToken = userService.queryByToken(userToken);
        if (userVoWithoutToken == null) {
            throw new BusinessException(ChatStatus.AUTHENTICATION_EXCEPTION,
                    "remoteAddr={}, method={}, requestURI={}, userToken={}",
                    remoteAddr, method, requestURI, userToken);
        }
        // 将查询到的userVo放入请求上下文中
        RequestContextHolder.set(userVoWithoutToken);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHolder.reset();
    }
}
