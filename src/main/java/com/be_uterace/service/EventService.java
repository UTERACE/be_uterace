package com.be_uterace.service;

import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.request.RunningCategoryDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.ClubPaginationResponse;
import com.be_uterace.payload.response.EventDetailResponse;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.ResponseObject;
import org.springframework.security.core.Authentication;

public interface EventService {
    EventPaginationResponse getEventPaginationEvent(int current_page, int per_page,String search_name, String ongoing);

    EventDetailResponse getEventDetail(Integer event_id);

    ResponseObject createEvent(CreateEventDto req, Authentication auth);

    ResponseObject updateEvent(UpdateEventDto req, Authentication authentication);

    ResponseObject deleteEvent(int event_id);
    ResponseObject addDistanceToEvent(int event_id, int distance_id, Authentication authentication);
    ResponseObject deleteDistanceFromEvent(int event_id, int distance_id, Authentication authentication);

    EventPaginationResponse getOwnEventCreated(int current_page, int per_page,String search_name, Authentication authentication);

    boolean checkJoinEvent(int event_id, Authentication auth);
}
