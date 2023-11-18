package com.be_uterace.payload.response.stravaresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ActivityStravaResponse {
    private int resource_state;
    private Athlete athlete;
    private String name;
    private double distance;
    private int moving_time;
    private int elapsed_time;
    private double total_elevation_gain;
    private String type;
    private String sport_type;
    private int workout_type;
    private long id;
    private double step_rate;
    private double average_heartrate;
    private double calories;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date start_date;

    private Date start_date_local;
    private String timezone;
    private double utc_offset;
    private String location_city;
    private String location_state;
    private String location_country;
    private int achievement_count;
    private int kudos_count;
    private int comment_count;
    private int athlete_count;
    private int photo_count;
    private Map map;

    private boolean trainer;
    private boolean commute;
    private boolean manual;

    // Sử dụng @JsonProperty để ánh xạ "_private" trong JSON thành trường "isPrivate" trong lớp Java
    @JsonProperty("_private")
    private boolean isPrivate;

    private String visibility;
    private boolean flagged;
    private String gear_id;
    private List<Double> start_latlng;
    private List<Double> end_latlng;
    private double average_speed;
    private double max_speed;
    private boolean has_heartrate;
    private boolean heartrate_opt_out;
    private boolean display_hide_heartrate_option;
    private double elev_high;
    private double elev_low;
    private long upload_id;
    private String upload_id_str;
    private String external_id;
    private boolean from_accepted_tag;
    private int pr_count;
    private int total_photo_count;
    private boolean has_kudoed;

    // Getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor
    public static class Athlete {
        private int id;
        private int resource_state;

        // Getters and setters
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor
    public static class Map {
        private String id;
        private String summary_polyline;
        private int resource_state;

        // Getters and setters
    }
}
