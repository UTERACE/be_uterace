package com.be_uterace.service;

import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.stravaresponse.WebhookResponse;

import java.io.IOException;
import java.text.ParseException;

public interface WebhookService {

    ResponseObject updateRunEventWebhook(WebhookResponse request);


    ResponseObject addRunEventWebhook(WebhookResponse request) throws IOException, ParseException;
}
