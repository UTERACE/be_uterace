package com.be_uterace.controller;

import com.be_uterace.payload.request.*;
import com.be_uterace.payload.response.*;
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
        ResponseObject responseObject = clubService.createClub(clubAddDto, authentication);
        return ResponseEntity.ok(responseObject);
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

    @GetMapping("/check-join-club/{club_id}")
    public ResponseEntity<Boolean> checkJoinClubController(@PathVariable Integer club_id, Authentication authentication) {
        Boolean responseObject = clubService.checkJoinClub(club_id,authentication);
        return ResponseEntity.ok(responseObject);
    }

    @PutMapping(value = {"/delete-activity"})
    public ResponseEntity<ResponseObject> deleteActivityController(@RequestBody DeleteActivityClub req){
        ResponseObject res = clubService.deleteActivity(req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/recent-active/{club_id}")
    public ResponseEntity<RecentActiveResponse> recentActiveController(
            @PathVariable Integer club_id,
            @RequestParam (defaultValue = "1" )int current_page,
            @RequestParam (defaultValue = "5" ) int per_page,
            @RequestParam(required = false) String search_name,
            @RequestParam(defaultValue = "48") int hour) {

        RecentActiveResponse recentActiveResponse = clubService.getRecentActivity(
                current_page,per_page,club_id,search_name,hour);
        return ResponseEntity.ok(recentActiveResponse);
    }

    @GetMapping(value = {"/rank-member/{club_id}"})
    public ResponseEntity<RankingMemberResponse> rankingMemberClubController(
            @PathVariable Integer club_id, @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam String search_name){
        RankingMemberResponse res = clubService.getScoreBoardClubMember(club_id,current_page,per_page,search_name);
        return ResponseEntity.ok(res);
    }
}
