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
@Table(name = "POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "TITLE")
    private String title;

    @ManyToOne
    @JoinColumn(name = "USER_CREATE", referencedColumnName = "USER_ID")
    private User userCreate;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    /*@Lob
    @Column(name = "HTML_CONTENT")
    private String htmlContent;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;*/

    @Column(name = "OUTSTANDING")
    private Boolean outstanding;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPDATE_AT")
    private Date updatedAt;

    @Column(name = "DELETED")
    private Boolean deleted;

    // Getters and setters
}
