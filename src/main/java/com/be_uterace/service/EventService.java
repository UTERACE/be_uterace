package com.be_uterace.service;

import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.EventDetailResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.ResponseObject;
import org.springframework.security.core.Authentication;

public interface EventService {
    EventPaginationResponse getEventPaginationEvent(int current_page, int per_page, boolean ongoing);

    EventDetailResponse getEventDetail(Integer event_id);

    ResponseObject createEvent(CreateEventDto req, Authentication auth);

    ResponseObject updateEvent(UpdateEventDto req);

    ResponseObject deleteEvent(int event_id);
}
