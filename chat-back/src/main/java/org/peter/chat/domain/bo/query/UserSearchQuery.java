package org.peter.chat.domain.bo.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询对象
 * 查询用户时,可输入的查询参数
 */
@Data
@Accessors(chain = true)
public class UserSearchQuery {
    // 用户编号
    private String userId;
    // 用户帐号
    private String username;
}
