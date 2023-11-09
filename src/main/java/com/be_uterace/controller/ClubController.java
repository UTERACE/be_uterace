package com.be_uterace.controller;

import com.be_uterace.payload.request.ClubAddDto;
import com.be_uterace.payload.request.ClubUpdateDto;
import com.be_uterace.payload.request.UserClubRequest;
import com.be_uterace.payload.response.ClubDetailResponse;
import com.be_uterace.payload.response.ClubPaginationResponse;
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
            @RequestParam int per_page,
            @RequestParam String search_name){
        ClubPaginationResponse clubPaginationResponse = clubService.getAllClub(
                current_page,per_page,search_name);
        return ResponseEntity.ok(clubPaginationResponse);
    }

    @GetMapping("/{club_id}")
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
        return clubService.createClub(clubAddDto, authentication);
    }

    @PutMapping()
    public ResponseEntity<ResponseObject> updateClubController(@RequestBody ClubUpdateDto clubUpdateDto, Authentication authentication) {
        return clubService.updateClub(clubUpdateDto, authentication);
    }

    @DeleteMapping("/{club_id}")
    public ResponseEntity<ResponseObject> deleteClubController(@PathVariable Integer club_id, Authentication authentication) {
        return clubService.deleteClub(club_id, authentication);
    }
    @DeleteMapping("/delete-member")
    public ResponseEntity<ResponseObject> deleteMemberController(@RequestBody UserClubRequest request) {
        return clubService.deleteMember(request);
    }
    @PutMapping("/change-admin")
    public ResponseEntity<ResponseObject> changeAdminController(@RequestBody UserClubRequest request) {
        return clubService.changeAdmin(request);
    }

    @GetMapping("/created-club")
    public ResponseEntity<ClubPaginationResponse> ownClubCreatedController(
            @RequestParam int current_page,
            @RequestParam int per_page,@RequestParam String search_name, Authentication authentication){
        ClubPaginationResponse clubPaginationResponse = clubService.getOwnClubCreated(
                current_page,per_page,search_name, authentication);
        return ResponseEntity.ok(clubPaginationResponse);
    }

    @GetMapping("/joined-club")
    public ResponseEntity<ClubPaginationResponse> clubJoinedController(
            @RequestParam int current_page,
            @RequestParam int per_page,@RequestParam String search_name, Authentication authentication){
        ClubPaginationResponse clubPaginationResponse = clubService.getClubJoined(
                current_page,per_page,search_name, authentication);
        return ResponseEntity.ok(clubPaginationResponse);
    }

    @PostMapping("/join-club/{club_id}")
    public ResponseEntity<ResponseObject> joinClubController(@PathVariable Integer club_id, Authentication authentication) {
        ResponseObject responseObject = clubService.joinClub(club_id,authentication);
        return ResponseEntity.ok(responseObject);
    }

    @DeleteMapping("/leave-club/{club_id}")
    public ResponseEntity<ResponseObject> leaveClubController(@PathVariable Integer club_id, Authentication authentication) {
        ResponseObject responseObject = clubService.leaveClub(club_id,authentication);
        return ResponseEntity.ok(responseObject);
    }


}
