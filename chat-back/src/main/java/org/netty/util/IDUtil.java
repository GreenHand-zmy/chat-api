package org.netty.util;

import java.util.UUID;

public final class IDUtil {
    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
