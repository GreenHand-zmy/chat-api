package org.peter.chat.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.peter.chat.domain.vo.common.UserCommonVO;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserWithTokenVO extends UserCommonVO {
    /**
     * 用户token
     */
    private String token;
}
