package org.peter.chat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Random;

@ConfigurationProperties(prefix = "chat.setting")
@Data
public class ChatProperties {
    private List<String> defaultNicknameList;

    public String getRandomNickname() {
        Random random = new Random();
        int index = random.nextInt(defaultNicknameList.size());
        return defaultNicknameList.get(index);
    }
}
