package com.be_uterace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SHIRT_SIZE")
public class ShirtSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIZE_ID")
    private Long sizeId;

    @Column(name = "SIZE_NAME")
    private String sizeName;

    // Getters and setters
}

