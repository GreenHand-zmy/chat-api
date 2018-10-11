package org.peter.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String id;

    @Column(nullable = false)
    private String username;

    private String password;
}
