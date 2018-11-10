package org.peter.chat.exception;

import lombok.Getter;
import org.peter.chat.enums.ChatStatus;
import org.slf4j.helpers.MessageFormatter;

@Getter
public class BusinessException extends RuntimeException {
    private int code;

    private String snapshot;

    public BusinessException(ChatStatus status, String snapshotFormat, Object... argArray) {
        super(status.getMessage());
        code = status.getCode();
        snapshot = MessageFormatter.format(snapshotFormat, argArray).getMessage();
    }

    public BusinessException(ChatStatus status) {
        super(status.getMessage());
        code = status.getCode();
    }
}
