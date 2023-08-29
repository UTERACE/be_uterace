package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_EVENT")
public class UserEvent {
    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

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

