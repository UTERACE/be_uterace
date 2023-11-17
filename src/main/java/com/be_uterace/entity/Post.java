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
    private Timestamp createdAt;

    @Column(name = "HTML_CONTENT", length = 10000)
    private String htmlContent;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "OUTSTANDING")
    private String outstanding;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID", referencedColumnName = "CLUB_ID")
    private Club club;

    // Getters and setters
}
