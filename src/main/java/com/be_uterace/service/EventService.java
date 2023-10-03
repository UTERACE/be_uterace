package com.be_uterace.service;

import com.be_uterace.payload.response.EventPaginationResponse;

public interface EventService {
    EventPaginationResponse getEventPaginationEvent(int current_page, int per_page, boolean ongoing);
}
