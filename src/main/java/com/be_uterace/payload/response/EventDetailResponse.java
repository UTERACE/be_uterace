package com.be_uterace.payload.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetailResponse {
    private Integer event_id;
    private String name;
    private String image;
    private String description;
    private Date from_date;
    private Date to_date;
    private List<RunningCategoryResponse> distance;
    private int total_member;
    private Double total_distance;
    private int total_activities;
    private int total_clubs;
    private int completed;
    private int not_completed;
    private int male;
    private int female;
    private Double min_pace;
    private Double max_pace;
    private String details;
    private String regulations;
    private String prize;
    private Boolean is_free;
    private Double registration_fee;
}
