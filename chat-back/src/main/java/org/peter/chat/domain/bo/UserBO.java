package org.peter.chat.domain.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserBO {
    /**
     * 用户编号
     */
    private String id;

    /**
     * 用户名
     */
    @NotEmpty
    @Length(min = 4, max = 20)
    @Pattern(regexp = "[a-zA-Z_-]{4,20}")
    private String username;
    /**
     * 密码
     */
    @NotEmpty
    @Length
    @Pattern(regexp = "[a-zA-z0-9]{6,20}")
    private String password;

    /**
     * 昵称
     */
    private String nickname;
}
