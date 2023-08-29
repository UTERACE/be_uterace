package com.be_uterace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLE_FUNCTION")
public class RoleFunction {
    @Id
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    private Role role;

    @Id
    @ManyToOne
    @JoinColumn(name = "FUNC_ID", referencedColumnName = "FUNC_ID")
    private Function function;

    // Getters and setters
}

