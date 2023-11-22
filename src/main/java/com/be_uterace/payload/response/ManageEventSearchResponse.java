package com.be_uterace.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageEventSearchResponse {
    private int per_page;
    private int current_page;
    private int total_page;
    private int total_events;
    private List<Event> events;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Event {
        private Integer event_id;
        private String name;
        private String image;
        private int total_members;
        private int total_activities;
        private String outstanding;
        private String status;
        private String reason_block;
    }
}
