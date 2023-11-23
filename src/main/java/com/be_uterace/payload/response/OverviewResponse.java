package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverviewResponse {
    private Integer event_id;
    private String title;
    private String content;
    private String image;
    private Integer total_members;
    private Integer total_activities;
}
