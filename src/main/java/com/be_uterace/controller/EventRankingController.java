package com.be_uterace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping(value = {"/ranking-users", "/ranking-clubs"})
public class EventRankingController {
    @GetMapping("/event-detail/{event_id}")
    public ResponseEntity<String> getEventDetail(@PathVariable("event_id") String eventId) {
        // Xử lý yêu cầu ở đây và phân biệt giữa /ranking-users và /ranking-clubs
        if (RequestContextHolder.currentRequestAttributes().getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST).equals("/ranking-users/event-detail/{event_id}")) {
            // Xử lý yêu cầu cho /ranking-users
            return ResponseEntity.ok("Handling /ranking-users/event-detail/" + eventId);
        } else {
            // Xử lý yêu cầu cho /ranking-clubs
            return ResponseEntity.ok("Handling /ranking-clubs/event-detail/" + eventId);
        }
    }
}
