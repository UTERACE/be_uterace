package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ScoreboardResponse {
    @JsonProperty("per_page")
    private int perPage;

    @JsonProperty("current_page")
    private int currentPage;

    @JsonProperty("total_page")
    private int totalPage;

    @JsonProperty("total_club")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int totalClub;

    @JsonProperty("ranking_club")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ClubRankingResponse> ranking_club;

    @JsonProperty("total_user")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalUser;

    @JsonProperty("ranking_user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<UserRankingResponse> ranking_user;
}
