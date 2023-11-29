package com.be_uterace.payload.response.stravaresponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class WebhookResponse {
    private String aspectType;
    private int eventTime;
    private int objectId;
    private String objectType;
    private int ownerId;
    private int subscriptionId;
    private Map<String, Object> updates;
}
