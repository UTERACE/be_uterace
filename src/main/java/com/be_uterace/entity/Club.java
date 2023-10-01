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
@Table(name = "CLUB")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUB_ID")
    private Long clubId;

    @Column(name = "CLUB_NAME")
    private String clubName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CLUB_TOTAL_DISTANCE")
    private Double clubTotalDistance;

    @Column(name = "CLUB_RANKING")
    private Integer clubRanking;

    @Column(name = "STATUS")
    private String status;

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

}


