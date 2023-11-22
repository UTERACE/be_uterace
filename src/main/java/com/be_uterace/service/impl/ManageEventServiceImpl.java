package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Event;
import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ManageEventSearchResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.service.ManageEventService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageEventServiceImpl implements ManageEventService {

    private EventRepository eventRepository;

    public ManageEventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseObject lockEvent(Integer event_id, LockRequest lockRequest) {
        eventRepository.markLockEvent("0",event_id, lockRequest.getReason());
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Khóa sự kiện thành công");
        return responseObject;
    }

    @Override
    public ResponseObject unlockEvent(Integer event_id) {
        eventRepository.markLockEvent("1",event_id, null);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Mở khóa sự kiện thành công");
        return responseObject;    }

    @Override
    public ResponseObject outstandingEvent(Integer event_id) {
        eventRepository.markOutstandingEvent("1",event_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Chọn sự kiện nổi bật thành công");
        return responseObject;    }

    @Override
    public ResponseObject notOutstandingEvent(Integer event_id) {
        eventRepository.markOutstandingEvent("0",event_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa sự kiện nổi bật thành công");
        return responseObject;    }

    @Override
    public ManageEventSearchResponse searchEvent(int current_page, int per_page, String search) {
        Page<Event> eventPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        if(search==null || search.equals("")) {
            eventPage = eventRepository.findAll(pageable);
        } else {
            eventPage = eventRepository.searchEventManage(search,pageable);
        }
        List<Event> clubList = eventPage.getContent();
        List<ManageEventSearchResponse.Event> eventListResponse = new ArrayList<>();
        for (Event event : clubList){
            ManageEventSearchResponse.Event eventResponse = new ManageEventSearchResponse.Event();
            eventResponse.setEvent_id(event.getEventId());
            eventResponse.setName(event.getTitle());
            eventResponse.setImage(event.getPicturePath());
            eventResponse.setTotal_members((event.getNumOfMales() != null ? event.getNumOfMales() : 0) + (event.getNumOfFemales() != null ? event.getNumOfFemales() : 0));
            eventResponse.setTotal_activities(event.getTotalActivities());
            eventResponse.setOutstanding(event.getOutstanding());
            eventResponse.setStatus(event.getStatus());
            eventResponse.setReason_block(event.getReason());
            eventListResponse.add(eventResponse);
        }
        return ManageEventSearchResponse.builder()
                .per_page(eventPage.getSize())
                .total_events((int) eventPage.getTotalElements())
                .current_page(eventPage.getNumber() + 1)
                .total_page(eventPage.getTotalPages())
                .events(eventListResponse)
                .build();
    }
}
