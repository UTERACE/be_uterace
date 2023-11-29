package com.be_uterace.payload.response.stravaresponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class WebhookResponse {
    private String aspect_type;
    private long event_time;
    private long object_id;
    private String object_type;
    private long owner_id;
    private long subscription_id;
    private Map<String, Object> updates;
}
