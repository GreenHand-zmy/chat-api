package org.peter.chat.enums;

import lombok.Getter;

@Getter
public enum CustomHttpHeader {
    X_USER_TOKEN("X_USER_TOKEN");

    private String headerName;

    CustomHttpHeader(String headerName) {
        this.headerName = headerName;
    }
}
