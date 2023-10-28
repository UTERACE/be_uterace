package com.be_uterace.controller;

import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.EventDetailResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@Transactional
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<EventPaginationResponse> eventPaginationController(
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam boolean ongoing){
        EventPaginationResponse eventPaginationResponse = eventService.getEventPaginationEvent(
                current_page,per_page,ongoing);
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
}
