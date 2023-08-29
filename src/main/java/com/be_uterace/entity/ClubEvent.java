package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLUB_EVENT")
public class ClubEvent {
    @Id
    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private Club club;

    @Id
    @ManyToOne
    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
    private Event event;

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
