package com.be_uterace.service.impl;

import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.service.ManageEventService;
import com.be_uterace.utils.StatusCode;
import org.springframework.stereotype.Service;

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
}
