package org.peter.chat.exception;

public class DataLengthLimitException extends ChatCheckException {
    // 最小长度
    private int min;

    // 最大长度
    private int max;

    public DataLengthLimitException(String message, int min, int max) {
        super(message + "[最小长度: " + min + ", 最大长度: " + max + " ]");
    }

    public DataLengthLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
