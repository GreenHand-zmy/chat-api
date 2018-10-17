package org.peter.chat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "chat.login-interceptor")
@Data
public class LoginInterceptorProperties {
    private List<String> include;
    private List<String> exclude;
}
