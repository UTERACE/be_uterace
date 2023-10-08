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
@Table(name = "EVENT")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long eventId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "PICTURE_PATH")
    private String picturePath;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "NUM_OF_ATTENDEE")
    private Integer numOfAttendee;

    @Column(name = "NUM_OF_RUNNER")
    private Integer numOfRunner;

    @Column(name = "NUM_OF_CLUBS")
    private Integer numOfClubs;

    @Column(name = "NUM_OF_COMPLETED")
    private Integer completed;

    @Column(name = "NUM_OF_NOT_COMPLETED")
    private Integer notCompleted;

    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "MAX_PACE")
    private Double maxPace;

    @Column(name = "OUTSTANDING")
    private Integer outstanding;

    @ManyToOne
    @JoinColumn(name = "USER_CREATE", referencedColumnName = "USER_ID")
    private User createUser;

    @Column(name = "MIN_PACE")
    private Double minPace;

    @ManyToOne
    @JoinColumn(name = "ADMIN", referencedColumnName = "USER_ID")
    private User adminUser;

    @Column(name = "NUM_OF_MALES")
    private Integer numOfMales;

    @Column(name = "NUM_OF_FEMALES")
    private Integer numOfFemales;

    @Column(name = "TOTAL_ACTIVITIES")
    private Integer totalActivities;

    @Column(name = "DETAILS")
    private String details;

    @Column(name = "REGULATIONS")
    private String regulations;

    @Column(name = "PRIZE")
    private String prize;


    // Getters and setters
}
