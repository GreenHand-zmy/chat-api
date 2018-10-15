package org.peter.chat.exception;

public class UserExistedCheckException extends ChatCheckException {
    public UserExistedCheckException(String message) {
        super(message);
    }

    public UserExistedCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
