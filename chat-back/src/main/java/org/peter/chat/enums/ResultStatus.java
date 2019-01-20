package org.peter.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果状态
 */
@AllArgsConstructor
@Getter
public enum ResultStatus implements BaseStatus{
    SUCCESS(200, "success"), FAIL(500, "failed");
    // 状态码
    private int code;

    // 状态信息
    private String info;
}
