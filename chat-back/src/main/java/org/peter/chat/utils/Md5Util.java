package org.peter.chat.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    private static final String DEFAULT_SLAT = "F4ABp52p2iNnyPByW0cOnfgB240NyQ3i";

    public static String md5Str(String value) {
        return md5Str(value, DEFAULT_SLAT);
    }


    public static String md5Str(String value, String slat) {
        String result = null;
        try {
            String data = value + slat;
            MessageDigest digest = MessageDigest.getInstance("md5");
            result = Base64.encodeBase64String(digest.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

/*    public static void main(String[] args) {
        System.out.println(md5Str("zmy", "asd"));
        System.out.println(md5Str("zmy"));
    }*/
}
