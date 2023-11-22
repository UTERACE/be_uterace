package com.be_uterace.controller;

import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ManageEventSearchResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ManageClubService;
import com.be_uterace.service.ManageEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-event")
public class ManageEventController {

    private ManageEventService manageEventService;

    public ManageEventController(ManageEventService manageEventService) {
        this.manageEventService = manageEventService;
    }

    @PostMapping("/lock/{event_id}")
    public ResponseEntity<ResponseObject> lockClub(@PathVariable Integer event_id, @RequestBody LockRequest lockRequest){
        ResponseObject responseObject = manageEventService.lockEvent(event_id, lockRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/unlock/{event_id}")
    public ResponseEntity<ResponseObject> unlockClub(@PathVariable Integer event_id){
        ResponseObject responseObject = manageEventService.unlockEvent(event_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/outstanding/{event_id}")
    public ResponseEntity<ResponseObject> outstanding(@PathVariable Integer event_id){
        ResponseObject responseObject = manageEventService.outstandingEvent(event_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/not-outstanding/{event_id}")
    public ResponseEntity<ResponseObject> notOutstanding(@PathVariable Integer event_id){
        ResponseObject responseObject = manageEventService.notOutstandingEvent(event_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping()
    public ResponseEntity<ManageEventSearchResponse> findAllUserStatus(
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam(required=false) String search_name) {
        ManageEventSearchResponse manageEventSearchResponse = manageEventService.searchEvent(current_page,per_page,search_name);
        return ResponseEntity.status(HttpStatus.OK).body(manageEventSearchResponse);
    }
}
