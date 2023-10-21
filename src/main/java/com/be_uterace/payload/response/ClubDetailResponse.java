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
public class ClubDetailResponse {
    private Integer club_id;
    private String image;
    private String name;
    private String description;
    private Integer total_member;
    private Double total_distance;
    private Integer total_activities;
    private Integer total_news;
    private Date created_at;
    private String manager;
    private Integer male;
    private Integer female;
    private Double min_pace;
    private Double max_pace;
    private List<PostResponse> news;
    private String details;
}
