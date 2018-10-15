package org.peter.chat.utils;

import org.peter.chat.exception.ChatCheckException;

public class CheckUtil {
    public static void check(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    private static void fail(String message) {
        throw new ChatCheckException(message);
    }
}
