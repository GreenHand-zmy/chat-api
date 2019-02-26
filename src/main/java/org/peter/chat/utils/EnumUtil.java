package org.peter.chat.utils;

import org.peter.chat.enums.BaseStatus;

public class EnumUtil {
    public static <T extends BaseStatus> T codeOf(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T t : enumConstants) {
            if (t.getCode() == code) {
                return t;
            }
        }
        return null;
    }
}
