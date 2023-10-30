package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class EventPaginationResponse {

    private int per_page;
    private int total_events;
    private int current_page;
    private int total_page;
    List<EventResponse> events;
}
