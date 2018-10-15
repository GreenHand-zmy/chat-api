package org.peter.chat.domain.vo;

import lombok.Data;

@Data
public class UserVo {
    /**
     * 用户编号
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像(小图)
     */
    private String faceImage;

    /**
     * 用户头像(大图)
     */
    private String faceImageBig;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户二维码
     */
    private String qrcode;
}
