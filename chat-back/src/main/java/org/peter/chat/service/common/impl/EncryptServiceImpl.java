package org.peter.chat.service.common.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.peter.chat.enums.exceptionStatus.ChatExceptionStatus;
import org.peter.chat.exception.BusinessException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class EncryptServiceImpl {
    private static final String KEY_ALGORITHM = "DES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";//默认的加密算法
    private static final String DEFAULT_KEY = "sde@5f98H*^hsff%dfs$r344&df8543*er";//默认的key

    public static String encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(DEFAULT_KEY));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
            log.error("", ex);
        }
        throw new BusinessException(ChatExceptionStatus.APP_RUNTIME_EXCEPTION);
    }

    public static String decrypt(String content) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(DEFAULT_KEY));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return null;
    }

    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());

            //DES 要求密钥长度为 56
            kg.init(56, random);

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为DES专用密钥
        } catch (NoSuchAlgorithmException ex) {

        }

        return null;
    }

    public static void main(String[] args) {
        String content = "hello";
        String encrypt = EncryptServiceImpl.encrypt(content);
        String decrypt = EncryptServiceImpl.decrypt(encrypt);

        System.out.println(encrypt);
        System.out.println(decrypt);
    }
}
