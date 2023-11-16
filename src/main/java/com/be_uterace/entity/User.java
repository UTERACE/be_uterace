package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"USER\"") // Specify the table name with double quotes
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_NAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "TEL_NUM")
    private String telNum;

    @Column(name = "AVATAR_PATH")
    private String avatarPath;

    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance;

    @Column(name = "PACE")
    private Double pace=0.0;

    @Column(name = "RANKING")
    private Integer ranking;

    @Column(name = "STATUS")
    private String status="1";

    @Column(name = "LINK_FB")
    private String linkFb;

    @Column(name = "HOME_NUMBER")
    private String homeNumber;

    @Column(name = "TYPE_ACCOUNT")
    private String typeAccount;

    @ManyToOne
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "ORG_ID", referencedColumnName = "ORG_ID")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "ORG_CHILD_ID", referencedColumnName = "ORG_ID")
    private Organization organization_child;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    )
    private Set<Role> roles;
    // Other fields, getters, and setters
    @Column(name = "STRAVA_ID")
    private Long stravaId;
    @Column(name = "STRAVA_ACCESS_TOKEN")
    private String stravaAccessToken;
    @Column(name = "STRAVA_REFRESH_TOKEN")
    private String stravaRefreshToken;
    @Column(name = "STRAVA_FULL_NAME")
    private String stravaFullName;
    @Column(name = "STRAVA_IMAGE")
    private String stravaImage;
    @Column(name = "SYNC_STATUS")
    private String syncStatus;

    @Column(name = "REASON")
    private String reason;

    @CreationTimestamp
    @Column(name = "LAST_SYNC")
    private Timestamp last_sync;
}

