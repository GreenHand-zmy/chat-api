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
public class FriendsRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关系表编号
     */
    private String id;

    /**
     * 本人编号
     */
    private String meUserId;

    /**
     * 好友编号
     */
    private String friendUserId;

    /**
     * 记录产生时间
     */
    private LocalDateTime gmtCreated;


}
