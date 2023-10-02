package com.be_uterace.payload.response;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingResponse {
    private Long user_id;
    private String first_name;
    private String last_name;
    private String image;
    private String gender;
    private Double pace;
    private Double total_distance;
    private Long ranking;
    private String organization;
}
