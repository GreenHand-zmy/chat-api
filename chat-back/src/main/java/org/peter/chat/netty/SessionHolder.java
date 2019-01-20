package org.peter.chat.netty;

import io.netty.channel.Channel;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionHolder {
    /**
     * 存放userId=>Channel映射
     */
    private static final Map<String, Channel> sessionMap = new ConcurrentHashMap<>();

    private SessionHolder() {
    }

    /**
     * 放入userId=>Channel映射
     *
     * @param userId
     * @param channel
     */
    public static void putSession(String userId, Channel channel) {
        sessionMap.put(userId, channel);
    }

    /**
     * 通过userId获取Channel连接
     *
     * @param userId
     * @return
     */
    public static Channel getSession(String userId) {
        return sessionMap.get(userId);
    }

    /**
     * 通过userId删除userId => Channel映射
     *
     * @param userId
     */
    public static void removeSession(String userId) {
        sessionMap.remove(userId);
    }

    public static void removeChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : sessionMap.entrySet()) {
            if (entry.getValue().compareTo(channel) == 0) {
                sessionMap.remove(entry.getKey());
            }
        }
    }

    /**
     * 输出映射关系
     */
    public static void showSession() {
        for (Map.Entry<String, Channel> channelEntry : sessionMap.entrySet()) {
            System.out.println(LocalDateTime.now() + " " + channelEntry.getKey() + "=>"
                    + channelEntry.getValue().id().asShortText());
        }
    }
}
