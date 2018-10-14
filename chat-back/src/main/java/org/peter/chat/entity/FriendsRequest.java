package org.peter.chat.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
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
public class FriendsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求编号
     */
    private String id;

    /**
     * 请求方
     */
    private String sendUserId;

    /**
     * 接受方
     */
    private String acceptUserId;

    /**
     * 请求时间
     */
    private LocalDateTime requestDateTime;


}
