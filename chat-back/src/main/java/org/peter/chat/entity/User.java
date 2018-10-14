package org.peter.chat.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zmy
 * @since 2018-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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

    /**
     * 用户终端编号
     */
    private String clientId;

    /**
     * 记录产生时间
     */
    private LocalDateTime gmtCreated;

    /**
     * 记录修改时间
     */
    private LocalDateTime gmtModified;


}
