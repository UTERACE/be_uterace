package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ROLE")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    // Getters and setters
}

