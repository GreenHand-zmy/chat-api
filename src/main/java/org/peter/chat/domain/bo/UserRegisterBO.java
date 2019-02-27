package org.peter.chat.domain.bo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户注册BO
 */
@Data
public class UserRegisterBO {
    /**
     * 用户名
     */
    @NotEmpty
//    @Length(min = 4, max = 20)
//    @Pattern(regexp = "[a-zA-Z_-]{4,20}")
    private String username;

    /**
     * 密码
     */
    @NotEmpty
//    @Length
//    @Pattern(regexp = "[a-zA-z0-9]{6,20}")
    private String password;

    /**
     * 昵称
     */
    @NotEmpty
    private String nickname;

    /**
     * 客户端id
     */
    private String clientId;
}
