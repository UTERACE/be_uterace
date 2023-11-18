package com.be_uterace.service.impl;

import com.be_uterace.entity.*;
import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.request.RunningCategoryDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.projection.UserRankingProjection;
import com.be_uterace.repository.*;
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
    private UserEventRepository userEventRepository;

    private final EntityManager em;
    private FileService fileService;
    @Value("${path.image}")
    private String path;


    public EventServiceImpl(EventRepository eventRepository, RunningCategoryRepository runningCategoryRepository,
                            EventRunningCategoryRepository eventRunningCategoryRepository, UserEventRepository userEventRepository,
                            UserRepository userRepository, EntityManager em, FileService fileService) {
        this.eventRepository = eventRepository;
        this.runningCategoryRepository = runningCategoryRepository;
        this.eventRunningCategoryRepository = eventRunningCategoryRepository;
        this.userRepository = userRepository;
        this.userEventRepository = userEventRepository;
        this.em = em;
        this.fileService = fileService;
    }

    @Override
    public EventPaginationResponse getEventPaginationEvent(int current_page, int per_page,String search_name, String ongoing) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<Event> eventPage;
        if (ongoing.equals("1"))
            eventPage= eventRepository.findEventsWithStatusOnGoing(search_name, pageable);
        else if (ongoing.equals("0"))
            eventPage = eventRepository.findEventsWithStatusFinished(search_name, pageable);
        else
            eventPage = eventRepository.findEventsWithStatusUpcoming(search_name, pageable);
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
                event.setOutstanding(0);
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
    public ResponseObject updateEvent(UpdateEventDto req, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)){
            return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
        }
        Event event = eventRepository.findEventByEventId(req.getEvent_id());
        if (event == null) {
            return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy giải chạy");
        }
        event.setTitle(req.getName() != null && !Objects.equals(req.getName(), "") ? req.getName() : event.getTitle());
        if (!event.getPicturePath().equals(req.getImage()) && !Objects.equals(req.getImage(), "")){
            if (Objects.equals(event.getPicturePath(), ""))
                event.setPicturePath(path + fileService.saveImage(req.getImage()));
            else if (fileService.deleteImage(event.getPicturePath())){
                System.out.println("Delete Image Successful");
                event.setPicturePath(path + fileService.saveImage(req.getImage()));
            }
        }
        event.setDescription(req.getDescription() != null && !Objects.equals(req.getDescription(), "") ? req.getDescription() : event.getDescription());
        event.setStartDate(req.getFrom_date() != null ? req.getFrom_date() : event.getStartDate());
        event.setEndDate(req.getTo_date() != null ? req.getTo_date() : event.getEndDate());
        event.setDetails(req.getDetails() != null && !Objects.equals(req.getDetails(), "") ? req.getDetails() : event.getDetails());
        event.setRegulations(req.getRegulations() != null && !Objects.equals(req.getRegulations(), "") ? req.getRegulations() : event.getRegulations());
        event.setPrize(req.getPrize() != null && !Objects.equals(req.getPrize(), "") ? req.getPrize() : event.getPrize());
        event.setMinPace(req.getMin_pace() != null ? req.getMin_pace() : event.getMinPace());
        event.setMaxPace(req.getMax_pace() != null ? req.getMax_pace() : event.getMaxPace());
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
                Page<Event> eventPage = eventRepository.findEventByJoinUserUserId(search_name, pageable, userOptional.get().getUserId());
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

    @Override
    public EventPaginationResponse getEventJoined(int current_page, int per_page, String search_name, Authentication authentication) {
        if (authentication !=null && authentication.getPrincipal() instanceof UserDetails userDetails){
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()){
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<Event> eventPage = eventRepository.findEventByJoinUserUserId(search_name, pageable, userOptional.get().getUserId());
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

    @Override
    public ResponseObject joinEvent(int event_id, Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Event> eventOptional = eventRepository.findById(event_id);
                if (eventOptional.isPresent()) {
                    Event event = eventOptional.get();
                    Optional<UserEvent> userEventOptional = userEventRepository.findByUserUserIdAndEventEventId(userOptional.get().getUserId(), event_id);
                    if (userEventOptional.isPresent()) {
                        return new ResponseObject(StatusCode.NOT_FOUND,"Bạn đã tham gia giải chạy này");
                    }
                    UserEvent userEvent = new UserEvent();
                    userEvent.setEvent(event);
                    userEvent.setUser(userOptional.get());
                    userEventRepository.save(userEvent);
                    event.setNumOfAttendee(event.getNumOfAttendee() + 1);
                    eventRepository.save(event);
                    return new ResponseObject(StatusCode.SUCCESS,"Tham gia giải chạy thành công");
                }
            }
        }
        return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
    }

    @Override
    public ResponseObject leaveEvent(int event_id, Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Event> eventOptional = eventRepository.findById(event_id);
                if (eventOptional.isPresent()) {
                    Event event = eventOptional.get();
                    Optional<UserEvent> userEventOptional = userEventRepository.findByUserUserIdAndEventEventId(userOptional.get().getUserId(), event_id);
                    if (userEventOptional.isPresent()) {
                        UserEvent userEvent = userEventOptional.get();
                        userEventRepository.delete(userEvent);
                        event.setNumOfAttendee(event.getNumOfAttendee() - 1);
                        eventRepository.save(event);
                        return new ResponseObject(StatusCode.SUCCESS,"Rời khỏi giải chạy thành công");
                    }
                }
                return new ResponseObject(StatusCode.NOT_FOUND,"Bạn chưa tham gia giải chạy này");
            }
        }
        return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
    }

    @Override
    public boolean checkJoinEvent(int event_id, Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<UserEvent> userEventOptional = userEventRepository.findByUserUserIdAndEventEventId(userOptional.get().getUserId(), event_id);
                return userEventOptional.isPresent();
            }
        }
        return false;
    }

}
