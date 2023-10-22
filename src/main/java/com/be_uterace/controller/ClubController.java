package com.be_uterace.controller;

import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.response.ClubDetailResponse;
import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/s{club_id}")
    public ResponseEntity<ClubDetailResponse> getClubDetail(@PathVariable Integer club_id) {
        ClubDetailResponse clubDetail = clubService.getClubDetail(club_id);
        if (clubDetail != null) {
            return new ResponseEntity<>(clubDetail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createClubController(@RequestBody ClubAddDto clubAddDto, Authentication authentication) {
        ResponseObject responseObject = clubService.createClub(clubAddDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @PutMapping()
    public ResponseEntity<ResponseObject> updateClubController(@RequestBody ClubUpdateDto clubUpdateDto, Authentication authentication) {
        ResponseObject responseObject = clubService.updateClub(clubUpdateDto, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/{club_id}")
    public ResponseEntity<ResponseObject> deleteClubController(@PathVariable Integer club_id, Authentication authentication) {
        ResponseObject responseObject = clubService.deleteClub(club_id, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
}
