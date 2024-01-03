package com.be_uterace.service.impl;

import com.be_uterace.entity.*;
import com.be_uterace.payload.request.CreateEventDto;
import com.be_uterace.payload.request.UpdateEventDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.repository.*;
import com.be_uterace.service.EventService;
import com.be_uterace.service.FileService;
import com.be_uterace.utils.StatusCode;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    private RunningCategoryRepository runningCategoryRepository;
    private EventRunningCategoryRepository eventRunningCategoryRepository;

    private UserRepository userRepository;
    private UserEventRepository userEventRepository;

    private UEActivityRepository ueActivityRepository;

    private final EntityManager em;
    private FileService fileService;


    public EventServiceImpl(EventRepository eventRepository, RunningCategoryRepository runningCategoryRepository,
                            EventRunningCategoryRepository eventRunningCategoryRepository, UserEventRepository userEventRepository,
                            UserRepository userRepository, EntityManager em,
                            FileService fileService,
                            UEActivityRepository ueActivityRepository) {
        this.eventRepository = eventRepository;
        this.runningCategoryRepository = runningCategoryRepository;
        this.eventRunningCategoryRepository = eventRunningCategoryRepository;
        this.userRepository = userRepository;
        this.userEventRepository = userEventRepository;
        this.em = em;
        this.fileService = fileService;
        this.ueActivityRepository = ueActivityRepository;
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
            eventResponse.setTotal_activities(event.getTotalActivities());
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
                    event.setPicturePath(fileService.saveImage(req.getImage()));
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
                event.setOutstanding("0");
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
                event.setPicturePath(fileService.saveImage(req.getImage()));
            else if (fileService.deleteImage(event.getPicturePath())){
                System.out.println("Delete Image Successful");
                event.setPicturePath(fileService.saveImage(req.getImage()));
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
        return new ResponseObject(StatusCode.SUCCESS,"Cập nhật giải chạy thành công");
    }

    @Override
    public ResponseObject deleteEvent(int event_id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)){
            return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy người dùng");
        }
        Optional<Event> eventOptional = eventRepository.findEventByEventIdAndAdminUser_UserId(event_id, userOptional.get().getUserId());
        if (eventOptional.isEmpty()) {
            return new ResponseObject(StatusCode.NOT_FOUND,"Không tìm thấy giải chạy");
        }
        eventRunningCategoryRepository.deleteAllByEvent_EventId(event_id);
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
                Page<Event> eventPage = eventRepository.findEventByCreateUserAndTitleContaining(userOptional.get().getUserId(), search_name, pageable);
                List<Event> eventList = eventPage.getContent();
                List<EventResponse> eventResponses = new ArrayList<>();
                for (Event event : eventList){
                    EventResponse eventResponse = new EventResponse();
                    eventResponse.setEvent_id(event.getEventId());
                    eventResponse.setName(event.getTitle());
                    eventResponse.setImage(event.getPicturePath());
                    eventResponse.setTotal_members(event.getNumOfAttendee());
                    eventResponse.setTotal_activities(event.getTotalActivities());
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
                    eventResponse.setTotal_activities(event.getTotalActivities());
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
    public EventPaginationResponse getEventCreatedByUser(Long user_id, int current_page, int per_page, String search_name) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<Event> eventPage = eventRepository.findEventByCreateUserAndTitleContaining(user_id, search_name, pageable);
        List<Event> eventList = eventPage.getContent();
        List<EventResponse> eventResponses = new ArrayList<>();
        for (Event event : eventList){
            EventResponse eventResponse = new EventResponse();
            eventResponse.setEvent_id(event.getEventId());
            eventResponse.setName(event.getTitle());
            eventResponse.setImage(event.getPicturePath());
            eventResponse.setTotal_members(event.getNumOfAttendee());
            eventResponse.setTotal_activities(event.getTotalActivities());
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
    public ResponseObject joinEvent(int event_id, Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Event> eventOptional = eventRepository.findEventsWithStatusOnGoing(event_id);
                if (eventOptional.isEmpty()) {
                    return new ResponseObject(StatusCode.NOT_FOUND,"Giải chạy chưa diễn ra hoặc đã kết thúc");
                }
                if (eventOptional.isPresent()) {
                    Event event = eventOptional.get();
                    Optional<UserEvent> userEventOptional = userEventRepository.findByUserUserIdAndEventEventId(userOptional.get().getUserId(), event_id);
                    if (userEventOptional.isPresent()) {
                        return new ResponseObject(StatusCode.NOT_FOUND,"Bạn đã tham gia giải chạy này");
                    }
                    UserEvent userEvent = new UserEvent();
                    userEvent.setEvent(event);
                    userEvent.setUser(userOptional.get());
                    userEvent.setJoinDate(new Date());
                    userEvent.setTotalDistance(0.0);
                    userEvent.setPace(0.0);
                    userEvent.setStatus_complete("0");
                    userEvent.setRunningCategory(runningCategoryRepository.findById(1).get());

                    userEventRepository.save(userEvent);
                    event.setNumOfAttendee(event.getNumOfAttendee() + 1);
                    if (Objects.equals(userOptional.get().getGender(), "Nam"))
                        event.setNumOfMales(event.getNumOfMales() + 1);
                    else
                        event.setNumOfFemales(event.getNumOfFemales() + 1);
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
                Optional<Event> eventOptional = eventRepository.findEventsWithStatusOnGoing(event_id);
                if (eventOptional.isPresent()) {
                    Event event = eventOptional.get();
                    Optional<UserEvent> userEventOptional = userEventRepository.findByUserUserIdAndEventEventId(userOptional.get().getUserId(), event_id);
                    if (userEventOptional.isPresent()) {
                        UserEvent userEvent = userEventOptional.get();
                        userEventRepository.delete(userEvent);
                        event.setNumOfAttendee(event.getNumOfAttendee() - 1);
                        if (Objects.equals(userOptional.get().getGender(), "Nam") && event.getNumOfMales() > 0)
                            event.setNumOfMales(event.getNumOfMales() - 1);
                        else if (Objects.equals(userOptional.get().getGender(), "Nữ") && event.getNumOfFemales() > 0)
                            event.setNumOfFemales(event.getNumOfFemales() - 1);
                        eventRepository.save(event);
                        return new ResponseObject(StatusCode.SUCCESS,"Rời khỏi giải chạy thành công");
                    }
                }
                return new ResponseObject(StatusCode.NOT_FOUND,"Giải chạy chưa diễn ra hoặc đã kết thúc");
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

    @Override
    public RankingMemberResponse getScoreBoardEventMember(int event_id, int current_page, int per_page, String search_name) {

        Pageable pageable = PageRequest.of(current_page-1, per_page);
        Event event = eventRepository.findById(event_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        Page<UserEvent> userEventPage;
        if(search_name==null || search_name.equals("")){
            userEventPage = userEventRepository.findByEventIdAndSearchName(event_id,null,pageable);
        }
        else userEventPage = userEventRepository.findByEventIdAndSearchName(event_id, search_name,pageable);
        List<UserEvent> userEventList = userEventPage.getContent();

        List<RankingMemberResponse.RankingUser> rankingUserList = new ArrayList<>();
        for (UserEvent userEvent : userEventList){
            RankingMemberResponse.RankingUser rankingUser = new RankingMemberResponse.RankingUser();
            User user = userEvent.getUser();
            rankingUser.setUser_id(user.getUserId());
            rankingUser.setRanking(userEvent.getRanking());
            rankingUser.setFirst_name(user.getFirstName());
            rankingUser.setLast_name(user.getLastName());
            rankingUser.setGender(user.getGender().name());
            rankingUser.setPace(userEvent.getPace());
            rankingUser.setImage(user.getAvatarPath());
            rankingUser.setTotal_distance(userEvent.getTotalDistance());
            rankingUser.setJoin_date(userEvent.getJoinDate());
            rankingUserList.add(rankingUser);
        }
        return RankingMemberResponse.builder()
                .per_page(userEventPage.getSize())
                .total_user((int) userEventPage.getTotalElements())
                .current_page(userEventPage.getNumber()+1)
                .total_page(userEventPage.getTotalPages())
                .ranking_user(rankingUserList)
                .build();
    }

    @Override
    public RecentActiveResponse getRecentActivity(int current_page, int per_page, Integer eventId, String search, int hours) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        Page<UserEventActivity> activityPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdDateTime = now.minusHours(hours);
        Timestamp thresholdTimestamp = Timestamp.valueOf(thresholdDateTime);

        if(search==null || search.isEmpty()) {
            activityPage = ueActivityRepository.findRunsByDateTimeAndName(thresholdTimestamp, null, eventId,pageable);
        } else {
            activityPage = ueActivityRepository.findRunsByDateTimeAndName(thresholdTimestamp, search,eventId,pageable);
        }
        List<UserEventActivity> userEventActivityList = activityPage.getContent();
        List<RecentActiveResponse.Activity> arrayList = new ArrayList<>();
        for (UserEventActivity userEventActivity : userEventActivityList){
            Run run = userEventActivity.getRun();
            RecentActiveResponse.Activity activityResponse = new RecentActiveResponse.Activity();
            activityResponse.setActivity_id(run.getRunId());
            activityResponse.setMember_id(run.getUser().getUserId());
            activityResponse.setMember_image(run.getUser().getAvatarPath());
            activityResponse.setMember_name(run.getUser().getFirstName()+" "+run.getUser().getLastName());
            activityResponse.setActivity_start_date(run.getCreatedAt());
            activityResponse.setActivity_distance(run.getDistance());
            activityResponse.setActivity_pace(run.getPace());
            activityResponse.setActivity_duration(run.getDuration());
            activityResponse.setActivity_name(run.getName());
            activityResponse.setActivity_type(run.getType());
            activityResponse.setActivity_link_strava("https://www.strava.com/activities/"+run.getStravaRunId());
            activityResponse.setActivity_map(run.getSummaryPolyline());
            activityResponse.setStatus(run.getStatus());
            activityResponse.setReason(run.getReason());
            arrayList.add(activityResponse);
        }
        return RecentActiveResponse.builder()
                .per_page(activityPage.getSize())
                .total_activities((int) activityPage.getTotalElements())
                .current_page(activityPage.getNumber() + 1)
                .total_page(activityPage.getTotalPages())
                .activities(arrayList)
                .build();
    }

    @Override
    public EventPaginationResponse getEventCompletedOrNot(Long user_id, int current_page, int per_page, String search_name, boolean complete) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<Event> eventPage;
        if(complete){
            eventPage = eventRepository.getEventEnded(pageable, search_name,user_id);
        }
        else eventPage = eventRepository.getEventInCurrentTime(pageable, search_name,user_id);

        List<Event> eventList = eventPage.getContent();
        List<EventResponse> eventResponses = new ArrayList<>();
        for (Event event : eventList){
            EventResponse eventResponse = new EventResponse();
            eventResponse.setEvent_id(event.getEventId());
            eventResponse.setName(event.getTitle());
            eventResponse.setImage(event.getPicturePath());
            eventResponse.setTotal_members(event.getNumOfAttendee());
            eventResponse.setTotal_activities(event.getTotalActivities());
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
    public EventPaginationResponse getEventCompletedOrNot(int current_page, int per_page, String search_name, boolean complete, Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<Event> eventPage;
                if(complete){
                    eventPage = eventRepository.getEventEnded(pageable, search_name,userOptional.get().getUserId());
                }
                else eventPage = eventRepository.getEventInCurrentTime(pageable, search_name,userOptional.get().getUserId());

                List<Event> eventList = eventPage.getContent();
                List<EventResponse> eventResponses = new ArrayList<>();
                for (Event event : eventList){
                    EventResponse eventResponse = new EventResponse();
                    eventResponse.setEvent_id(event.getEventId());
                    eventResponse.setName(event.getTitle());
                    eventResponse.setImage(event.getPicturePath());
                    eventResponse.setTotal_members(event.getNumOfAttendee());
                    eventResponse.setTotal_activities(event.getTotalActivities());
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
