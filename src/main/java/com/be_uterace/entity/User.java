package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "\"USER\"") // Specify the table name with double quotes
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "TEL_NUM")
    private String telNum;

    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance;

    @Column(name = "RANKING")
    private Integer ranking;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "LINK_FB")
    private String linkFb;

    @Column(name = "HOME_NUMBER")
    private String homeNumber;

    @ManyToOne
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "SIZE_ID", referencedColumnName = "SIZE_ID")
    private ShirtSize shirtSize;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", referencedColumnName = "ORG_ID")
    private Organization organization;

    // Other fields, getters, and setters
}

