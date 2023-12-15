package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubRankingResponse {
    private Integer club_id;
    private Integer ranking;
    private String name;
    private String image;
    private Double total_distance;
    private Integer total_members;
    private Long total_activities;
}