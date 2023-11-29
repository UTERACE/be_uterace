package com.be_uterace.controller;

import com.be_uterace.payload.response.stravaresponse.WebhookResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

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

//    @PostMapping("/")
//    public ResponseEntity<Object> processWebhook(@RequestBody WebhookResponse res) {
//        // Assuming WebhookResponse class is created to match the structure of the incoming JSON
//        // You need to implement updateRunEventWebhook and addRunEventWebhook methods accordingly
//
//        if (Router.RE_INIT_STATUS) {
//            tempDataList.add(res);
//            return ResponseEntity.ok().build();
//        }
//
//        if ("update".equals(res.getAspectType())) {
//            updateRunEventWebhook(res, db); // Implement this method
//        } else {
//            addRunEventWebhook(res, db); // Implement this method
//        }
//
//        return ResponseEntity.ok().build();
//    }

//    @PostMapping("/webhook")
//    public ResponseEntity<String> processWebhook(@RequestBody String request) {
////        WebhookResponse res = new WebhookResponse(
////                request.getAspectType(),
////                request.getEventTime(),
////                request.getObjectId(),
////                request.getObjectType(),
////                request.getOwnerId(),
////                request.getSubscriptionId()
////        );
//
//        System.out.println(request);
//        return  null;
//    }
}
