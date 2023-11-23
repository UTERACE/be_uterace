package com.be_uterace.payload.response;

import com.be_uterace.entity.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class HomePageResponse {
    List<OverviewResponse> overview;
    List<ClubRankingResponse> ranking_club;
    List<RankingUserHomeResponse> ranking_user;
    List<EventResponse> events;
    List<ClubResponse> clubs;
    List<PostHomeResponse> news;
    Map<String, Object> statistic;
}
