package com.be_uterace.entity;

import com.be_uterace.utils.key.UserClubId;
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
@Table(name = "USER_CLUB")
@IdClass(UserClubId.class)
public class UserClub {
    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private Club club;

    @CreationTimestamp
    @Column(name = "JOIN_DATE")
    private Timestamp joinDate;

    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance = 0.0;

    @Column(name = "RANKING")
    private Integer ranking;

    @Column(name = "PACE")
    private Double pace = 0.0;

    @Column(name = "STATUS")
    private String status = "1";
    // Getters and setters
}
