package com.be_uterace.controller;

import com.be_uterace.payload.response.ScoreboardClubResponse;
import com.be_uterace.payload.response.ScoreboardUserResponse;
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
    public ResponseEntity<?> getScoreboardController(
            @RequestParam String ranking,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam(required=false) String search_name
    ) {
        if (Objects.equals(ranking, "club")){
            ScoreboardClubResponse scoreboardClubResponse = scoreboardService.getScoreboardClub(
                    month,year,current_page,per_page, search_name);
            return ResponseEntity.ok(scoreboardClubResponse);
        } else if (Objects.equals(ranking, "user")) {
            ScoreboardUserResponse scoreboardUserResponse = scoreboardService.getScoreboardUser(
                    month,year,current_page,per_page,search_name);
            return ResponseEntity.ok(scoreboardUserResponse);
        }
        else return null;
    }
}
