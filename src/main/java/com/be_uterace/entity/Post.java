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
@Table(name = "POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Integer postId;

    @Column(name = "TITLE", unique = true)
    private String title;

    @ManyToOne
    @JoinColumn(name = "USER_CREATE", referencedColumnName = "USER_ID")
    private User userCreate;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Timestamp createdAt = new Timestamp(new Date().getTime());

    @Column(name = "HTML_CONTENT", columnDefinition = "TEXT")
    private String htmlContent;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "OUTSTANDING")
    private String outstanding;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private Club club;

    @Column(name = "REASON")
    private String reason;

    // Getters and setters
}
