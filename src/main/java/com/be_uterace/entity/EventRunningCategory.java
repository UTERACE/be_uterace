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
@Table(name = "EVENT_RUNNING_CATEGORY")
public class EventRunningCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_RUNNING_CATEGORY_ID")
    private Integer eventRunningCategoryId;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "RUNNING_CATEGORY_ID", referencedColumnName = "RUNNING_CATEGORY_ID")
    private RunningCategory runningCategory;
}
