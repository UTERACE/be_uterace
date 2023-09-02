package com.be_uterace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ORGANIZATION")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORG_ID")
    private Long orgId;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "ORG_PARENT_ID")
    private Integer orgParentId;

    // Getters and setters
}
