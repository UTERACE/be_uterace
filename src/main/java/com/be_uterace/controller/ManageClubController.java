package com.be_uterace.controller;

import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ManageUserStatusResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ManageClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-club")
public class ManageClubController {
    private ManageClubService manageClubService;

    public ManageClubController(ManageClubService manageClubService) {
        this.manageClubService = manageClubService;
    }

    @PostMapping("/lock/{club_id}")
    public ResponseEntity<ResponseObject> lockClub(@PathVariable Integer club_id){
        ResponseObject responseObject = manageClubService.lockClub(club_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/unlock/{club_id}")
    public ResponseEntity<ResponseObject> unlockClub(@PathVariable Integer club_id){
        ResponseObject responseObject = manageClubService.unlockClub(club_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/outstanding/{club_id}")
    public ResponseEntity<ResponseObject> outstanding(@PathVariable Integer club_id){
        ResponseObject responseObject = manageClubService.outstandingClub(club_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/not-outstanding/{club_id}")
    public ResponseEntity<ResponseObject> notOutstanding(@PathVariable Integer club_id){
        ResponseObject responseObject = manageClubService.notOutstandingClub(club_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping()
    public ResponseEntity<ManageClubSearchResponse> findAllUserStatus(
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam(required=false) String search_name) {
        ManageClubSearchResponse manageClubSearchResponse = manageClubService.searchCLub(current_page,per_page,search_name);
        return ResponseEntity.status(HttpStatus.OK).body(manageClubSearchResponse);
    }
}
