package org.netty.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Session {
    private String userId;
    private String username;
}
