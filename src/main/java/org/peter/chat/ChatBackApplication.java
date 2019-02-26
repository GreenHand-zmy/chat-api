package org.peter.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@MapperScan("org.peter.chat.mapper")
@ComponentScan(basePackages = {"org.peter.chat", "org.n3r.idworker"})
public class ChatBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatBackApplication.class, args);
    }
}
