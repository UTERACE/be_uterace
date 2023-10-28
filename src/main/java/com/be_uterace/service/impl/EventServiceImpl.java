package com.be_uterace.service.impl;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.RunningCategory;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.repository.RunningCategoryRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.EventService;
import com.be_uterace.utils.StatusCode;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    private RunningCategoryRepository runningCategoryRepository;

    private UserRepository userRepository;

    private final EntityManager em;

    public EventServiceImpl(EventRepository eventRepository, RunningCategoryRepository runningCategoryRepository, UserRepository userRepository, EntityManager em) {
        this.eventRepository = eventRepository;
        this.runningCategoryRepository = runningCategoryRepository;
        this.userRepository = userRepository;
        this.em = em;
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

    @Override
    public EventDetailResponse getEventDetail(Integer event_id) {
        Event event = eventRepository.findEventByEventId(event_id);
        List<RunningCategory> runningCategory = runningCategoryRepository.findRunningCategoriesByEvent(event);
        List<RunningCategoryResponse> categoryResponse = new ArrayList<>();
        for (RunningCategory item : runningCategory){
            RunningCategoryResponse response = new RunningCategoryResponse();
            response.setId(item.getRunningCategoryID());
            response.setName(item.getRunningCategoryName());
            response.setDistance(item.getRunningCategoryDistance());
            categoryResponse.add(response);
        }
        return EventDetailResponse.builder()
                .event_id(event.getEventId())
                .image(event.getPicturePath())
                .name(event.getTitle())
                .description(event.getDescription())
                .from_date(event.getStartDate())
                .to_date(event.getEndDate())
                .distance(categoryResponse)
                .total_member(event.getNumOfAttendee())
                .total_distance(event.getTotalDistance())
                .total_activities(event.getTotalActivities())
                .total_clubs(event.getNumOfClubs())
                .completed(event.getCompleted())
                .not_completed(event.getNotCompleted())
                .male(event.getNumOfMales())
                .female(event.getNumOfFemales())
                .min_pace(event.getMinPace())
                .max_pace(event.getMaxPace())
                .details(event.getDetails())
                .regulations(event.getRegulations())
                .prize(event.getPrize())
                .build();
    }

    @Override
    public ResponseObject createEvent(CreateEventDto req, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {

                Event event = new Event();
                event.setTitle(req.getName());
                event.setPicturePath(req.getImage());
                event.setDescription(req.getDescription());
                event.setStartDate(req.getFrom_date());
                event.setEndDate(req.getTo_date());
                event.setDetails(req.getDetails());
                event.setRegulations(req.getRegulations());
                event.setPrize(req.getPrize());
                event.setMinPace(req.getMin_pace());
                event.setMaxPace(req.getMax_pace());
                event.setAdminUser(userOptional.get());
                eventRepository.save(event);
                em.refresh(event);
                List<RunningCategoryResponse> runningCategories = req.getDistance();
                for(RunningCategoryResponse item : runningCategories){
                    RunningCategory runningCategory = new RunningCategory();
                    runningCategory.setRunningCategoryID(item.getId());
                    runningCategory.setEvent(event);
                    runningCategory.setRunningCategoryName(item.getName());
                    runningCategory.setRunningCategoryDistance(item.getDistance());
                    runningCategoryRepository.save(runningCategory);
                }
                ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Tạo giải chạy thành công");
                return responseObject;

            }
        }
        return null;
    }

    @Override
    public ResponseObject updateEvent(UpdateEventDto req) {
        Event event = eventRepository.findEventByEventId(req.getEvent_id());
        event.setTitle(req.getName());
        event.setPicturePath(req.getImage());
        event.setDescription(req.getDescription());
        event.setStartDate(req.getFrom_date());
        event.setEndDate(req.getTo_date());
        event.setDetails(req.getDetails());
        event.setRegulations(req.getRegulations());
        event.setPrize(req.getPrize());
        event.setMinPace(req.getMin_pace());
        event.setMaxPace(req.getMax_pace());
        eventRepository.save(event);

        List<RunningCategory> runningCategoriesEntity = runningCategoryRepository.findRunningCategoriesByEvent(event);

        List<RunningCategoryResponse> runningCategories = req.getDistance();
        for (RunningCategoryResponse item : runningCategories) {
            for (RunningCategory runningCategory : runningCategoriesEntity) {
                if (Objects.equals(item.getId(), runningCategory.getRunningCategoryID())) {
                    runningCategory.setRunningCategoryName(item.getName());
                    runningCategory.setRunningCategoryDistance(item.getDistance());
                    runningCategoryRepository.save(runningCategory);
                    break;
                }
            }
        }

        return new ResponseObject(StatusCode.SUCCESS,"Cập nhật giải chạy thành công");
    }

    @Override
    public ResponseObject deleteEvent(int event_id) {
        eventRepository.deleteById(event_id);
        return new ResponseObject(StatusCode.SUCCESS,"Xóa giải chạy thành công");

    }
}
