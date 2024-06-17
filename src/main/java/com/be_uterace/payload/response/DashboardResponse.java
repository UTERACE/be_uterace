package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DashboardResponse {
    private int total_events;
    private int new_events;
    private int total_clubs;
    private int new_clubs;
    private int total_users;
    private int new_users;
    private int total_news;
    private int new_news;
    private List<Integer> chart_active_users;
    private List<Integer> chart_clubs;
    private List<Integer> chart_events;
    private List<Integer> chart_news;
    private List<Integer> chart_events_status;
}
