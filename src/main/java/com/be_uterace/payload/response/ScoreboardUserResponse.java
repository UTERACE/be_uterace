package com.be_uterace.payload.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreboardUserResponse {
    private int per_page;
    private int total_user;
    private int current_page;
    private int total_page;
    private List<UserRanking> ranking_user;

    // getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRanking {
        private Long user_id;
        private Integer ranking;
        private String first_name;
        private String last_name;
        private String image;
        private String gender;
        private Double pace;
        private Double total_distance;
        private String organization;

        // getters and setters
    }
}
