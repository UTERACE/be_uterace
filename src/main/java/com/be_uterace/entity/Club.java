package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLUB")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUB_ID")
    private Integer clubId;

    @Column(name = "CLUB_NAME", unique = true, length = 100)
    private String clubName;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Timestamp createdAt = new Timestamp(new Date().getTime());

    @Column(name = "CLUB_TOTAL_DISTANCE")
    private Double clubTotalDistance = 0.0;

    @Column(name = "CLUB_RANKING")
    private Integer clubRanking;

    @Column(name = "STATUS")
    private String status = "1";

    @ManyToOne
    @JoinColumn(name = "ADMIN", referencedColumnName = "USER_ID")
    private User adminUser;

    @Column(name = "MIN_PACE")
    private Double minPace;

    @Column(name = "MAX_PACE")
    private Double maxPace;

    @Column(name = "PICTURE_PATH")
    private String picturePath;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID", referencedColumnName = "USER_ID")
    private User creatorUser;

    @Column(name = "NUM_OF_ATTENDEE")
    private Long numOfAttendee = 0L;

    @Column(name = "NUM_OF_MALES")
    private Integer numOfMales = 0;

    @Column(name = "NUM_OF_FEMALES")
    private Integer numOfFemales = 0;

    @Column(name = "TOTAL_ACTIVITIES")
    private Long totalActivities = 0L;

    @Column(name = "DETAILS", columnDefinition = "TEXT")
    private String details;

    @Column(name = "OUTSTANDING")
    private String outstanding = "0";

    @Column(name = "REASON")
    private String reason;


}


