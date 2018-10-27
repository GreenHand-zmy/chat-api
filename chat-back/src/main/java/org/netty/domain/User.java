package org.netty.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private String userId;
    private String username;
    private String password;
}
