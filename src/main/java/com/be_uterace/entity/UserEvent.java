package com.be_uterace.entity;

import com.be_uterace.utils.key.UserClubId;
import com.be_uterace.utils.key.UserEventId;
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
@Table(name = "USER_EVENT")
@IdClass(UserEventId.class)
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

