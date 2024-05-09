package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RUN")
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUN_ID")
    private Long runId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @Column(name = "STRAVA_RUN_ID")
    private Long stravaRunId;

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

    @Column(name = "TYPE")
    private String type;

    @Column(name = "HEART_RATE")
    private Double heartRate;

    @Column(name = "STEP_RATE")
    private Double stepRate;
    @Column(name = "SUMMARY_POLYLINE", columnDefinition = "TEXT")
    private String summaryPolyline;

    @Column(name = "REASON")
    private String reason;

    // Getters and setters
}

