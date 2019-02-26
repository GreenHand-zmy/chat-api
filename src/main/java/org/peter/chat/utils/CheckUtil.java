package org.peter.chat.utils;

public class CheckUtil {
    public static void check(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    private static void fail(String message) {
        throw new RuntimeException(message);
    }
}
