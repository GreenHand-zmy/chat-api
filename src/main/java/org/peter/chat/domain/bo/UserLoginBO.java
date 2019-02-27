package org.peter.chat.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginBO {
    /**
     * 用户名
     */
    @NotEmpty
    private String username;
    /**
     * 密码
     */
    @NotEmpty
    private String password;
}
