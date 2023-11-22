package com.be_uterace.payload.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
public class ManageClubSearchResponse {
    private int per_page;
    private int current_page;
    private int total_page;
    private int total_clubs;
    private List<Club> clubs;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Club {
        private int club_id;
        private String name;
        private String image;
        private int total_members;
        private Double total_distance;
        private String outstanding;
        private String status;
        private String reason_block;
    }
}