package com.be_uterace.mapper;

import com.be_uterace.entity.Event;
import com.be_uterace.payload.EventDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class EventMapper {
//    public static EventDto convertFromEventToEventDto(Event event) {
//        return EventDto.builder()
//                .eventId(event.getEventId())
//                .description(event.getDescription())
//                .title(event.getTitle())
//                .createAt(event.getCreateAt())
//                .startDate(event.getStartDate())
//                .endDate(event.getEndDate())
//                .status(event.getStatus())
//                .runningCategoryId(event.getRunningCategoryId())
//                .numOfAttendee(event.getNumOfAttendee())
//                .numOfRunner(event.getNumOfRunner())
//                .totalDistance(event.getTotalDistance())
//                .build();
//    }
}
