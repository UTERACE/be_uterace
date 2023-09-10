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
@Table(name = "LOG_POST")
public class LogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "POST_ID", referencedColumnName = "POST_ID")
    private Post post;

    @Column(name = "TITLE_BEFORE")
    private String titleBefore;

    /*@Lob
    @Column(name = "CONTENT_BEFORE")
    private String contentBefore;

    @Lob
    @Column(name = "IMAGE_BEFORE")
    private byte[] imageBefore;*/

    @Column(name = "TITLE_AFTER")
    private String titleAfter;

    /*@Lob
    @Column(name = "CONTENT_AFTER")
    private String contentAfter;

    @Lob
    @Column(name = "IMAGE_AFTER")
    private byte[] imageAfter;*/

    @ManyToOne
    @JoinColumn(name = "USER_MODIFY", referencedColumnName = "USER_ID")
    private User userModify;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "DESCRIPTION_BEFORE")
    private String descriptionBefore;

    @Column(name = "DESCRIPTION_AFTER")
    private String descriptionAfter;

    @Column(name = "ACTION")
    private String action;

    // Getters and setters
}

