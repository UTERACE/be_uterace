package com.be_uterace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "AREA")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AREA_ID")
    private Long areaId;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "PRECINCT")
    private String precinct;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "STATUS")
    private String status;

    // Getters and setters
}

