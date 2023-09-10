package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_EVENT_ACTIVITY")
public class UserEventActivity {
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
    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
    private UserEvent userEvent;

    @Column(name = "REASON")
    private String reason;

    // Getters and setters
}

