package com.be_uterace.entity;

import com.be_uterace.utils.key.RunningCategoryId;
import com.be_uterace.utils.key.UserClubId;
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
@Table(name = "RUNNING_CATEGORY")
public class RunningCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUNNING_CATEGORY_ID")
    private Integer runningCategoryID;

    @Column(name = "RUNNING_CATEGORY_NAME")
    private String runningCategoryName;

    @Column(name = "RUNNING_CATEGORY_DISTANCE")
    private Double runningCategoryDistance;

}
