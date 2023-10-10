package com.be_uterace.controller;

import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    private ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public ResponseEntity<ClubPaginationResponse> clubPaginationController(
            @RequestParam int current_page,
            @RequestParam int per_page){
        ClubPaginationResponse clubPaginationResponse = clubService.getAllClub(
                current_page,per_page);
        return ResponseEntity.ok(clubPaginationResponse);
    }
}
