package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

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

    /*@Lob
    @Column(name = "PICTURE_PATH")
    private byte[] picturePath;*/

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "RUNNING_CATEGORY")
    private Double runningCategory;

    @Column(name = "NUM_OF_ATTENDEE")
    private Integer numOfAttendee;

    @Column(name = "NUM_OF_RUNNER")
    private Integer numOfRunner;

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

    // Getters and setters
}
