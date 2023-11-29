package com.be_uterace.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticResponse {
    private Long user_id;
    private String image;
    private Double total_distance;
    private Integer total_clubs;
    private Integer total_event;
    private Integer total_activities;
    private Integer ranking;
    private Double avg_pace;
    private String first_name;
    private String last_name;
    private String strava_user_link;
    private List<ChartDate> chart_date;
    private List<ChartMonth> chart_month;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChartDate {
        private String date_time;
        private Double date_distance;
        private Double date_pace;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChartMonth {
        private String month_time;
        private Double month_distance;
        private Double month_pace;

    }
}
