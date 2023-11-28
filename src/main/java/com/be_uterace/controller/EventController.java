package com.be_uterace.controller;

import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.service.EventService;
import com.be_uterace.service.UEActivityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@Transactional
public class EventController {
    private EventService eventService;
    private UEActivityService ueActivityService;

    public EventController(EventService eventService, UEActivityService ueActivityService) {
        this.eventService = eventService;
        this.ueActivityService = ueActivityService;
    }

    @GetMapping
    public ResponseEntity<EventPaginationResponse> eventPaginationController(
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam String ongoing,
            @RequestParam String search_name){
        EventPaginationResponse eventPaginationResponse = eventService.getEventPaginationEvent(
                current_page,per_page,search_name,ongoing);
        return ResponseEntity.ok(eventPaginationResponse);
    }

    @GetMapping(value = {"/{event_id}"})
    public ResponseEntity<EventDetailResponse> eventPaginationController(@PathVariable Integer event_id){
        EventDetailResponse eventDetailResponse = eventService.getEventDetail(event_id);

        return ResponseEntity.ok(eventDetailResponse);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addEventController(@Valid @RequestBody CreateEventDto req, Authentication auth){
        ResponseObject res = eventService.createEvent(req, auth);
        return ResponseEntity.ok(res);
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateEventController(@RequestBody UpdateEventDto req, Authentication authentication){
        ResponseObject res = eventService.updateEvent(req, authentication);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping(value = {"/{event_id}"})
    public ResponseEntity<ResponseObject> deleteEventController(@PathVariable Integer event_id){
        ResponseObject res = eventService.deleteEvent(event_id);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value = {"/{event_id}/add-distance/{distance_id}"})
    public ResponseEntity<ResponseObject> addDistanceToEventController(@PathVariable int event_id,
                                                                       @PathVariable int distance_id,
                                                                       Authentication authentication){
        ResponseObject res = eventService.addDistanceToEvent(event_id, distance_id, authentication);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping(value = {"/{event_id}/delete-distance/{distance_id}"})
    public ResponseEntity<ResponseObject> deleteDistanceFromEventController(@PathVariable int event_id,
                                                                       @PathVariable int distance_id,
                                                                       Authentication authentication){
        ResponseObject res = eventService.deleteDistanceFromEvent(event_id, distance_id, authentication);
        return ResponseEntity.ok(res);
    }

    @PutMapping(value = {"/delete-activity"})
    public ResponseEntity<ResponseObject> deleteActivityController(@RequestBody DeleteActivityEvent req){
        ResponseObject res = ueActivityService.deleteActivity(req);
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = {"/created-event"})
    public ResponseEntity<EventPaginationResponse> getEventCreatedController(@RequestParam int current_page,
                                                                    @RequestParam int per_page,
                                                                    @RequestParam String search_name,
                                                                    Authentication auth){
        EventPaginationResponse res = eventService.getOwnEventCreated(current_page, per_page,search_name,auth);
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = {"/joined-event"})
    public ResponseEntity<EventPaginationResponse> getEventJoinedController(@RequestParam int current_page,
                                                                    @RequestParam int per_page,
                                                                    @RequestParam String search_name,
                                                                    Authentication auth){
        EventPaginationResponse res = eventService.getEventJoined(current_page, per_page,search_name,auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value = {"/join-event/{event_id}"})
    public ResponseEntity<ResponseObject> joinEventController(@PathVariable Integer event_id, Authentication auth){
        ResponseObject res = eventService.joinEvent(event_id, auth);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value = {"/leave-event/{event_id}"})
    public ResponseEntity<ResponseObject> leaveEventController(@PathVariable Integer event_id, Authentication auth){
        ResponseObject res = eventService.leaveEvent(event_id, auth);
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = {"/check-join-event/{event_id}"})
    public ResponseEntity<Boolean> checkJoinEventController(@PathVariable Integer event_id, Authentication auth){
        Boolean res = eventService.checkJoinEvent(event_id, auth);
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = {"/rank-member/{event_id}"})
    public ResponseEntity<RankingMemberResponse> rankingMemberEventController(
            @PathVariable Integer event_id, @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam String search_name){
        RankingMemberResponse res = eventService.getScoreBoardEventMember(event_id,current_page,per_page,search_name);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/recent-active/{event_id}")
    public ResponseEntity<RecentActiveResponse> recentActiveController(
            @PathVariable Integer event_id,
            @RequestParam (defaultValue = "1" )int current_page,
            @RequestParam (defaultValue = "5" ) int per_page,
            @RequestParam(required = false) String search_name,
            @RequestParam(defaultValue = "48") int hour) {

        RecentActiveResponse recentActiveResponse = eventService.getRecentActivity(
                current_page,per_page,event_id,search_name,hour);
        return ResponseEntity.ok(recentActiveResponse);
    }
}
