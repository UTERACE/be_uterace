package com.be_uterace.service;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface EventService {
    EventPaginationResponse getEventPaginationEvent(int current_page, int per_page,String search_name, String ongoing);

    EventDetailResponse getEventDetail(Integer event_id);

    ResponseObject createEvent(CreateEventDto req, Authentication auth);

    ResponseObject updateEvent(UpdateEventDto req, Authentication authentication);

    ResponseObject deleteEvent(int event_id, Authentication authentication);
    ResponseObject addDistanceToEvent(int event_id, int distance_id, Authentication authentication);
    ResponseObject deleteDistanceFromEvent(int event_id, int distance_id, Authentication authentication);

    EventPaginationResponse getOwnEventCreated(int current_page, int per_page,String search_name, Authentication authentication);

    EventPaginationResponse getEventJoined(int current_page, int per_page,String search_name, Authentication authentication);

    EventPaginationResponse getEventCreatedByUser(Long user_id, int current_page, int per_page,String search_name);

    ResponseObject joinEvent(int event_id, User user);

    ResponseObject leaveEvent(int event_id, Authentication auth);

    boolean checkJoinEvent(int event_id, Authentication auth);

    RankingMemberResponse getScoreBoardEventMember(int event_id, int current_page, int per_page, String search_name);

    RecentActiveResponse getRecentActivity(int current_page, int per_page, Integer eventId, String search, int hours);

    EventPaginationResponse getEventCompletedOrNot(Long user_id,int current_page, int per_page, String search_name, boolean complete);

    EventPaginationResponse getEventCompletedOrNot(int current_page, int per_page, String search_name, boolean complete, Authentication auth);

    ResponseEntity<byte[]> exportScoreBoard(int event_id);

    EntryCodeResponse getEntryCode(Integer event_id);
}
