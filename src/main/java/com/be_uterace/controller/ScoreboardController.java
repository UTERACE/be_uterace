package com.be_uterace.controller;

import com.be_uterace.payload.request.ScoreboardDto;
import com.be_uterace.payload.response.HomePageResponse;
import com.be_uterace.payload.response.ScoreboardResponse;
import com.be_uterace.service.ScoreboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/scoreboard")
public class ScoreboardController {

    private ScoreboardService scoreboardService;

    public ScoreboardController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }

    @GetMapping
    public ResponseEntity<ScoreboardResponse> getScoreboardController(@RequestParam String ranking, @RequestBody ScoreboardDto scoreboardDto) {
        if (Objects.equals(ranking, "club")){
            ScoreboardResponse scoreboardResponse = scoreboardService.getScoreboardClub(
                    scoreboardDto.getMonth(),scoreboardDto.getYear(),
                    scoreboardDto.getCurrent_page(),scoreboardDto.getPer_page()
            );
            return ResponseEntity.ok(scoreboardResponse);
        } else if (Objects.equals(ranking, "user")) {
            return null;
        }
        else return null;
    }
}
