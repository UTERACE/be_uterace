package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingUserHomeResponse {
    private Long user_id;
    private Integer ranking;
    private String first_name;
    private String last_name;
    private String image;
    private Double total_distance;
    private Double pace;
}
