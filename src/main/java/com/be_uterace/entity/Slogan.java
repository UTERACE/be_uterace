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

