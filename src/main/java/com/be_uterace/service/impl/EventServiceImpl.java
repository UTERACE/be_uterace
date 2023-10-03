package com.be_uterace.service.impl;

import com.be_uterace.entity.Event;
import com.be_uterace.payload.response.EventPaginationResponse;
import com.be_uterace.payload.response.EventResponse;
import com.be_uterace.payload.response.ScoreboardResponse;
import com.be_uterace.payload.response.UserRankingResponse;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventPaginationResponse getEventPaginationEvent(int current_page, int per_page, boolean ongoing) {
        String ongoingAsString = (ongoing) ? "1" : "0";
        Pageable pageable = PageRequest.of(current_page, per_page);
        Page<Event> eventPage = eventRepository.findAllByStatusAndEndDate(ongoingAsString,pageable);
        List<Event> eventList = eventPage.getContent();
        List<EventResponse> eventResponses = new ArrayList<>();
        for (Event event : eventList){
            EventResponse eventResponse = new EventResponse();
            eventResponse.setEvent_id(event.getEventId());
            eventResponse.setName(event.getTitle());
            eventResponse.setImage(event.getPicturePath());
            eventResponse.setTotal_members(event.getNumOfAttendee());
            eventResponse.setTotal_clubs(event.getNumOfClubs());
            eventResponses.add(eventResponse);
        }
        return EventPaginationResponse.builder()
                .per_page(eventPage.getSize())
                .total_event((int) eventPage.getTotalElements())
                .current_page(eventPage.getNumber())
                .totalPage(eventPage.getTotalPages())
                .events(eventResponses)
                .build();
    }
}
