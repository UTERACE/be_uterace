package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_CLUB")
public class UserClub {
    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private Club club;

    @Column(name = "JOIN_DATE")
    private Date joinDate;

    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance;

    @Column(name = "RANKING")
    private Integer ranking;

    @Column(name = "PACE")
    private Double pace;

    // Getters and setters
}
