package com.be_uterace.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SLOGAN")
public class Slogan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SLOGAN_ID")
    private Long sloganId;

    @Column(name = "HTML_CONTENT")
    private String htmlContent;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "OUTSTANDING")
    private Boolean outstanding;

    // Getters and setters
}

