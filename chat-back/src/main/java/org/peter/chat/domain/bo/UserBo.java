package org.peter.chat.domain.bo;

import lombok.Data;

@Data
public class UserBo {
    /**
     * 用户编号
     */
    private String id;

    /**
     * 用户头像
     */
    private String faceData;
}
