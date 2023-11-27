package com.be_uterace.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreboardClubResponse {
    private int per_page;
    private int total_club;
    private int current_page;
    private int total_page;
    private List<RankedClub> ranking_club;

    // Getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RankedClub {
        private Integer club_id;
        private Integer ranking;
        private String name;
        private String image;
        private Double total_distance;
        private Integer total_members;
        private Long total_activities;

        // Getters and setters

        // Constructors
    }
}
