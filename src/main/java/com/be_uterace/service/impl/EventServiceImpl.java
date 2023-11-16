package com.be_uterace.service.impl;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.EventRunningCategory;
import com.be_uterace.entity.RunningCategory;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.request.RunningCategoryDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.repository.EventRunningCategoryRepository;
import com.be_uterace.repository.RunningCategoryRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.EventService;
import com.be_uterace.service.FileService;
import com.be_uterace.utils.StatusCode;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
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
    private EventRunningCategoryRepository eventRunningCategoryRepository;

    private UserRepository userRepository;

    private final EntityManager em;
    private FileService fileService;
    @Value("${path.image}")
    private String path;


    public EventServiceImpl(EventRepository eventRepository, RunningCategoryRepository runningCategoryRepository,
                            EventRunningCategoryRepository eventRunningCategoryRepository,
                            UserRepository userRepository, EntityManager em, FileService fileService) {
        this.eventRepository = eventRepository;
        this.runningCategoryRepository = runningCategoryRepository;
        this.eventRunningCategoryRepository = eventRunningCategoryRepository;
        this.userRepository = userRepository;
        this.em = em;
        this.fileService = fileService;
    }

    @Override
    public EventPaginationResponse getEventPaginationEvent(int current_page, int per_page,String search_name, boolean ongoing) {
        String ongoingAsString = (ongoing) ? "1" : "0";
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<Event> eventPage = eventRepository.findEventsWithStatusAndSearchName(ongoingAsString,search_name, pageable);
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
                .total_events((int) eventPage.getTotalElements())
                .current_page(eventPage.getNumber() + 1)
                .total_page(eventPage.getTotalPages())
                .events(eventResponses)
                .build();
    }

    @Override
    public EventDetailResponse getEventDetail(Integer event_id) {
        Event event = eventRepository.findEventByEventId(event_id);
        List<RunningCategory> runningCategory = runningCategoryRepository.findRunningCategoriesByEvent_EventId(event_id);
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
                if (!Objects.equals(req.getImage(), "") && req.getImage() != null)
                    event.setPicturePath(path+fileService.saveImage(req.getImage()));
                else
                    event.setPicturePath("");
                event.setDescription(req.getDescription());
                event.setStartDate(req.getFrom_date());
                event.setEndDate(req.getTo_date());
                event.setDetails(req.getDetails());
                event.setRegulations(req.getRegulations());
                event.setPrize(req.getPrize());
                event.setMinPace(req.getMin_pace());
                event.setMaxPace(req.getMax_pace());
                event.setAdminUser(userOptional.get());
                event.setCreateUser(userOptional.get());
                eventRepository.save(event);
                em.refresh(event);
                List<RunningCategoryResponse> runningCategories = req.getDistance();
                for(RunningCategoryResponse item : runningCategories){
                    RunningCategory runningCategory = runningCategoryRepository.findById(item.getId()).orElse(null);
                    EventRunningCategory eventRunningCategory = new EventRunningCategory();
                    eventRunningCategory.setEvent(event);
                    eventRunningCategory.setRunningCategory(runningCategory);
                    eventRunningCategoryRepository.save(eventRunningCategory);
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

//        List<RunningCategory> runningCategoriesEntity = runningCategoryRepository.findRunningCategoriesByEvent(event);
//
//        List<RunningCategoryResponse> runningCategories = req.getDistance();
//        for (RunningCategoryResponse item : runningCategories) {
//            for (RunningCategory runningCategory : runningCategoriesEntity) {
//                if (Objects.equals(item.getId(), runningCategory.getRunningCategoryID())) {
//                    runningCategory.setRunningCategoryName(item.getName());
//                    runningCategory.setRunningCategoryDistance(item.getDistance());
//                    runningCategoryRepository.save(runningCategory);
//                    break;
//                }
//            }
//        }

        return new ResponseObject(StatusCode.SUCCESS,"Cập nhật giải chạy thành công");
    }

    @Override
    public ResponseObject deleteEvent(int event_id) {
        eventRepository.deleteById(event_id);
        return new ResponseObject(StatusCode.SUCCESS,"Xóa giải chạy thành công");

    }

    @Override
    public ResponseObject addDistanceToEvent(int event_id, int distance_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (!userOptional.isPresent()) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
            }
            User user = userOptional.get();
            Event event = eventRepository.findEventByEventId(event_id);
            if (event == null) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy giải chạy");
            }
            if (!Objects.equals(user.getUserId(), event.getAdminUser().getUserId())) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không có quyền thêm khoảng cách");
            }
            RunningCategory runningCategory = runningCategoryRepository.findById(distance_id).orElse(null);
            if (runningCategory == null) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy khoảng cách");
            }
            EventRunningCategory eventRunningCategory = eventRunningCategoryRepository.findEventRunningCategoryByEventAndRunningCategory(event, runningCategory);
            if (eventRunningCategory != null) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Khoảng cách đã tồn tại");
            }
            eventRunningCategory = new EventRunningCategory();
            eventRunningCategory.setEvent(event);
            eventRunningCategory.setRunningCategory(runningCategory);
            eventRunningCategoryRepository.save(eventRunningCategory);
            return new ResponseObject(StatusCode.SUCCESS,"Thêm khoảng cách thành công");
        }
        return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
    }

    @Override
    public ResponseObject deleteDistanceFromEvent(int event_id, int distance_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (!userOptional.isPresent()) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
            }
            User user = userOptional.get();
            Event event = eventRepository.findEventByEventId(event_id);
            if (event == null) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy giải chạy");
            }
            if (!Objects.equals(user.getUserId(), event.getAdminUser().getUserId())) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không có quyền xóa khoảng cách");
            }
            RunningCategory runningCategory = runningCategoryRepository.findById(distance_id).orElse(null);
            if (runningCategory == null) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy khoảng cách");
            }
            EventRunningCategory eventRunningCategory = eventRunningCategoryRepository.findEventRunningCategoryByEventAndRunningCategory(event, runningCategory);
            if (eventRunningCategory == null) {
                return new ResponseObject(StatusCode.NOT_FOUND,"Khoảng cách không tồn tại");
            }
            eventRunningCategoryRepository.delete(eventRunningCategory);
            return new ResponseObject(StatusCode.SUCCESS,"Xóa khoảng cách thành công");
        }
        return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
    }

    @Override
    public EventPaginationResponse getOwnEventCreated(int current_page, int per_page,String search_name, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<Event> eventPage = eventRepository.findEventByCreateUserAndTitleContaining(userOptional.get(),search_name, pageable);
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
                        .total_events((int) eventPage.getTotalElements())
                        .current_page(eventPage.getNumber() + 1)
                        .total_page(eventPage.getTotalPages())
                        .events(eventResponses)
                        .build();

            }
        }
        return null;
    }

}
