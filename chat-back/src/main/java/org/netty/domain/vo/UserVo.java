package org.netty.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVo {
    private String userId;
    private String username;
}
