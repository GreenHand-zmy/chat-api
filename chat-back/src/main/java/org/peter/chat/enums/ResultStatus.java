package org.peter.chat.enums;

/**
 * 结果状态
 */
public enum ResultStatus {
    SUCCESS(200, "success"), FAIL(500, "failed");
    // 状态码
    private int code;

    // 状态信息
    private String info;

    ResultStatus(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
