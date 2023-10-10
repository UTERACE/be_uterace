package com.be_uterace.controller;

import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.response.ConnectStravaResponse;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.StravaOauthResponse;
import com.be_uterace.service.StravaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/strava")
public class StravaController {
    private StravaService stravaService;
    public StravaController(StravaService stravaService) {
        this.stravaService = stravaService;
    }
    @PostMapping(value = {"/connect?state=&code={code}&scope={scope}&activity={activity}"})
    public ConnectStravaResponse connect(@PathVariable String code, @PathVariable String scope, @PathVariable String activity) {
        return stravaService.connectStrava(code);
    }
}
