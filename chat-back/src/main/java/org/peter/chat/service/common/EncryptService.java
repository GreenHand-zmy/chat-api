package org.peter.chat.service.common;

public interface EncryptService {
    String encrypt(String content);

    String decrypt(String content);
}
