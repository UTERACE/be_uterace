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
@Table(name = "USER_CLUB_ACTIVITY")
public class UserClubActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RUN_ID", referencedColumnName = "RUN_ID")
    private Run run;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private UserClub userClub;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REASON")
    private String reason;

    // Getters and setters
}

