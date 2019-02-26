package org.peter.chat.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public final class SecureRandoms {
    private static final int DEFAULT_HEX_LENGTH = 32;

    /**
     * 返回随机多少位随机16进制
     *
     * @param length
     * @return
     */
    public static String nextHex(int length) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[length + 1 >> 1];
        random.nextBytes(bytes);
        String hex = DatatypeConverter.printHexBinary(bytes);
        hex = hex.substring(0, length);
        return hex;
    }

    public static String nextHex() {
        return nextHex(DEFAULT_HEX_LENGTH);
    }
}
