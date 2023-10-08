package com.be_uterace.controller;

import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.response.EventDetailResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
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

    @GetMapping(value = {"/event-detail/{eventId}"})
    public ResponseEntity<EventDetailResponse> eventPaginationController(@PathVariable Long eventId){
        EventDetailResponse eventDetailResponse = eventService.getEventDetail(eventId);

        return ResponseEntity.ok(eventDetailResponse);
    }
}
