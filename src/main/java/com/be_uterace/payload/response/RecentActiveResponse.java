package com.be_uterace.payload.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class RecentActiveResponse {
    private int per_page;
    private int current_page;
    private int total_activities;
    private int total_page;
    private List<Activity> activities;

    // Getters and setters for all fields

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Activity {
        private Long activity_id;
        private Long member_id;
        private String member_image;
        private String member_name;
        private Date activity_start_date;
        private Double activity_distance;
        private Double activity_pace;
        private String activity_duration;
        private String activity_name;
        private String activity_type;
        private String activity_link_strava;
        private String activity_map;
        private String status;
        private String reason;
    }
}
