package org.peter.chat.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserResetPasswordBo {
    /**
     * 用户名
     */
    @NotEmpty
    private String username;

    /**
     * 旧密码
     */
    @NotEmpty
    private String oldPassword;

    /**
     * 新密码
     */
    @NotEmpty
    private String newPassword;
}
