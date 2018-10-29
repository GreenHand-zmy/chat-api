package org.netty.attribute;

import io.netty.util.AttributeKey;
import org.netty.common.Session;

public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
