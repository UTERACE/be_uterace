package com.be_uterace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {
    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    private Role role;

    // Getters and setters
}
