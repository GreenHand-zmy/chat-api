package org.netty.util;

import io.netty.channel.Channel;
import org.netty.attribute.Attributes;
import org.netty.common.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionUtil {
    // userId -> channel 的映射
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

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

    public static List<Session> getAllSession() {
        List<Session> allSession = new ArrayList<>();
        for (Map.Entry<String, Channel> entry : userIdChannelMap.entrySet()) {
            Session session = getSession(entry.getValue());
            if (session != null) {
                allSession.add(session);
            }
        }
        return allSession;
    }

    public static List<Channel> getAllChannel() {
        List<Channel> channelList = new ArrayList<>();
        for (Map.Entry<String, Channel> entry : userIdChannelMap.entrySet()) {
            channelList.add(entry.getValue());
        }

        return channelList;
    }
}
