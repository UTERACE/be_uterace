package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SHIRT_SIZE")
public class ShirtSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIZE_ID")
    private Integer sizeId;

    @Column(name = "SIZE_NAME")
    private String sizeName;
}
