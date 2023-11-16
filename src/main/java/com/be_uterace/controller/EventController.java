package com.be_uterace.controller;

import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.EventDetailResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.UEActivityRepository;
import com.be_uterace.service.EventService;
import com.be_uterace.service.UEActivityService;
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
            @RequestParam boolean ongoing,
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
    public ResponseEntity<ResponseObject> addEventController(@RequestBody CreateEventDto req, Authentication auth){
        ResponseObject res = eventService.createEvent(req, auth);
        return ResponseEntity.ok(res);
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateEventController(@RequestBody UpdateEventDto req){
        ResponseObject res = eventService.updateEvent(req);
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
}
