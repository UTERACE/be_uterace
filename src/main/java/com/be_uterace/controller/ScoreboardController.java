package com.be_uterace.controller;

import com.be_uterace.payload.response.ScoreboardResponse;
import com.be_uterace.service.ScoreboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/scoreboard")
public class ScoreboardController {

    private ScoreboardService scoreboardService;

    public ScoreboardController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }

    @GetMapping
    public ResponseEntity<ScoreboardResponse> getScoreboardController(
            @RequestParam String ranking,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam int current_page,
            @RequestParam int per_page
    ) {
        if (Objects.equals(ranking, "club")){
            ScoreboardResponse scoreboardResponse = scoreboardService.getScoreboardClub(
                    month,year,current_page,per_page);
            return ResponseEntity.ok(scoreboardResponse);
        } else if (Objects.equals(ranking, "user")) {
            ScoreboardResponse scoreboardResponse = scoreboardService.getScoreboardUser(
                    month,year,current_page,per_page);
            return ResponseEntity.ok(scoreboardResponse);
        }
        else return null;
    }
}
