package org.peter.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.peter.chat.mapper")
public class ChatBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatBackApplication.class, args);
    }
}
