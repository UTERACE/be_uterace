package com.be_uterace.service;

import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ManageEventSearchResponse;
import com.be_uterace.payload.response.ResponseObject;

public interface ManageEventService {

    ResponseObject lockEvent(Integer event_id, LockRequest lockRequest);

    ResponseObject unlockEvent(Integer event_id);

    ResponseObject outstandingEvent(Integer event_id);

    ResponseObject notOutstandingEvent(Integer event_id);

    ManageEventSearchResponse searchEvent(int current_page, int per_page, String search);
}
