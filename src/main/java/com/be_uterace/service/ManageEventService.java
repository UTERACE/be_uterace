package com.be_uterace.service;

import com.be_uterace.payload.response.ResponseObject;

public interface ManageEventService {

    ResponseObject lockEvent(Integer event_id);

    ResponseObject unlockEvent(Integer event_id);

    ResponseObject outstandingEvent(Integer event_id);

    ResponseObject notOutstandingEvent(Integer event_id);
}
