package com.be_uterace.service;

import com.be_uterace.payload.response.EventRankingMemberResponse;
import com.be_uterace.payload.response.ScoreboardClubResponse;
import com.be_uterace.payload.response.ScoreboardUserResponse;

public interface ScoreboardService {
    ScoreboardClubResponse getScoreboardClub(int month, int year, int current_page, int per_page, String search_name);

    ScoreboardUserResponse getScoreboardUser(int month, int year, int current_page, int per_page, String search_name);
}
