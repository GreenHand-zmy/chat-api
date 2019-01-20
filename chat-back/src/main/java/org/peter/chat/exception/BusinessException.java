package org.peter.chat.exception;

import lombok.Getter;
import org.peter.chat.enums.BaseStatus;
import org.slf4j.helpers.MessageFormatter;

@Getter
public class BusinessException extends RuntimeException {
    private int code;

    private String snapshot;

    public BusinessException(BaseStatus status, String snapshotFormat, Object... argArray) {
        super(status.getInfo());
        code = status.getCode();
        snapshot = MessageFormatter.format(snapshotFormat, argArray).getMessage();
    }

    public BusinessException(BaseStatus status) {
        super(status.getInfo());
        code = status.getCode();
    }
}
