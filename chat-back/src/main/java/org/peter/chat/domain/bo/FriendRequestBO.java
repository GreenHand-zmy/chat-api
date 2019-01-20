package org.peter.chat.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.domain.bo.enums.FriendRequestType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class FriendRequestBO {
    // 请求编号
    @NotEmpty
    private String requestId;

    // 处理请求类型
    @NotNull
    private FriendRequestType status;
}
