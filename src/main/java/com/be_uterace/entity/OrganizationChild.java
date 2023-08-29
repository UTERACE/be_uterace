package com.be_uterace.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "ORGANIZATION_CHILD")
public class OrganizationChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHILD_ID")
    private Long childId;

    @Column(name = "CHILD_NAME")
    private String childName;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", referencedColumnName = "ORG_ID")
    private Organization organization;

    // Getters and setters
}

