package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_CLUB_ACTIVITY")
public class UserClubActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "RUN_ID")
    private Integer runId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISTANCE")
    private Double distance;

    @Column(name = "DURATION")
    private String duration;

    @Column(name = "PACE")
    private Double pace;

    @Column(name = "CALORI")
    private Double calori;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private UserClub userClub;

    @Column(name = "REASON")
    private String reason;

    // Getters and setters
}

