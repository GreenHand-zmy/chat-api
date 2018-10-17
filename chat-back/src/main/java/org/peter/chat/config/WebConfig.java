package org.peter.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.peter.chat.config.properties.ChatProperties;
import org.peter.chat.config.properties.LoginInterceptorProperties;
import org.peter.chat.web.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({LoginInterceptorProperties.class, ChatProperties.class})
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private LoginInterceptorProperties properties;

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("添加登录拦截器成功,不拦截exclude={}", properties.getExclude());

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(properties.getInclude())
                .excludePathPatterns(properties.getExclude());
    }
}
