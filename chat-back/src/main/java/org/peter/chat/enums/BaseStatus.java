package org.peter.chat.enums;

public interface BaseStatus {
    /**
     * 获取状态值
     *
     * @return
     */
    int getCode();

    /**
     * 状态值说明
     *
     * @return
     */
    String getInfo();
}
