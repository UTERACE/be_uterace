package com.be_uterace.controller;

import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.stravaresponse.WebhookResponse;
import com.be_uterace.service.WebhookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    private static final String VERIFY_TOKEN = "STRAVA";

    @GetMapping()
    public ResponseEntity<Object> verifyWebhook(@RequestParam(name = "hub.verify_token") String hubVerifyToken,
                                                @RequestParam(name = "hub.challenge") String hubChallenge,
                                                @RequestParam(name = "hub.mode") String hubMode) {
        if ("subscribe".equals(hubMode) && VERIFY_TOKEN.equals(hubVerifyToken)) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("hub.challenge", hubChallenge);
            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> processWebhook(@RequestBody WebhookResponse request) throws IOException, ParseException {
        if (Objects.equals(request.getAspect_type(), "update")) {
            ResponseObject res = webhookService.updateRunEventWebhook(request);
            return ResponseEntity.ok(res);
        } else {
            ResponseObject res = webhookService.addRunEventWebhook(request);
            return ResponseEntity.ok(res);
        }
    }
}
