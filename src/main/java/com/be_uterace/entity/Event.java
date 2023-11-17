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
@Table(name = "EVENT")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Integer eventId;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "TITLE")
    private String title;

    @CreationTimestamp
    @Column(name = "CREATE_AT")
    private Timestamp createAt;

    @Column(name = "PICTURE_PATH")
    private String picturePath;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS")
    private String status="1";

    @Column(name = "NUM_OF_ATTENDEE")
    private Integer numOfAttendee=0;

    @Column(name = "NUM_OF_RUNNER")
    private Integer numOfRunner=0;

    @Column(name = "NUM_OF_CLUBS")
    private Integer numOfClubs=0;

    @Column(name = "NUM_OF_COMPLETED")
    private Integer completed=0;

    @Column(name = "NUM_OF_NOT_COMPLETED")
    private Integer notCompleted=0;

    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance=0.0;

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
    private Integer numOfMales=0;

    @Column(name = "NUM_OF_FEMALES")
    private Integer numOfFemales=0;

    @Column(name = "TOTAL_ACTIVITIES")
    private Integer totalActivities=0;

    @Column(name = "DETAILS")
    private String details;

    @Column(name = "REGULATIONS")
    private String regulations;

    @Column(name = "PRIZE")
    private String prize;


    // Getters and setters
}
