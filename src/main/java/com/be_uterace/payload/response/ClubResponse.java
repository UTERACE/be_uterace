package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponse {
    private int club_id;
    private String name;
    private String image;
    private Long total_member;
    private Double total_distance;
}
