package org.peter.chat.domain.vo.common;

import lombok.Data;
import org.peter.chat.entity.UserEntity;
import org.springframework.beans.BeanUtils;


@Data
public class UserCommonVO {
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

    public static UserCommonVO transTo(UserEntity userEntity) {
        UserCommonVO instance = new UserCommonVO();
        BeanUtils.copyProperties(userEntity, instance);
        return instance;
    }
}
