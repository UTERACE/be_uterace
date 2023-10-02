package com.be_uterace.service;

import com.be_uterace.payload.response.ScoreboardResponse;

public interface ScoreboardService {
    ScoreboardResponse getScoreboardClub(int month, int year, int current_page, int per_page);

    ScoreboardResponse getScoreboardUser(int month, int year, int current_page, int per_page);
}
