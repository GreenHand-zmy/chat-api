package org.netty.util;

import io.netty.channel.Channel;
import org.netty.common.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionUtil {
    // userId -> channel 的映射
    public static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }


    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }
}
