package org.peter.chat.exception;

public class ChatCheckException extends RuntimeException {
    public ChatCheckException(String message) {
        super(message);
    }

    public ChatCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
