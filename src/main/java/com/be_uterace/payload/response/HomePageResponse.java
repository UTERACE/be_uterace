package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class HomePageResponse {
    List<Map<String, Object>> overview;
    List<Map<String, Object>> ranking_club;
    List<Map<String, Object>> ranking_user;
    List<Map<String, Object>> events;
    List<Map<String, Object>> clubs;
    List<Map<String, Object>> news;
    Map<String, Object> statistic;
}
