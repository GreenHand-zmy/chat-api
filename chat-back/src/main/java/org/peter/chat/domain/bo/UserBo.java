package org.peter.chat.domain.bo;

import lombok.Data;

@Data
public class UserBo {
    /**
     * 用户编号
     */
    private String id;

    /**
     * 昵称
     */
    private String nickname;
}
