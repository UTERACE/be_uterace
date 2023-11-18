package com.be_uterace.controller;

import com.be_uterace.payload.request.ConnectStravaRequest;
import com.be_uterace.payload.response.ConnectStravaResponse;
import com.be_uterace.service.StravaService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/strava")
public class StravaController {
    private StravaService stravaService;
    public StravaController(StravaService stravaService) {
        this.stravaService = stravaService;
    }
    @PostMapping(value = {"/connect"})
    public ConnectStravaResponse connect(@RequestBody ConnectStravaRequest connectStravaRequest) throws IOException {
        return stravaService.connectStrava(connectStravaRequest.getCode());
    }
    @PostMapping(value = {"/disconnect"})
    public ConnectStravaResponse disconnect() {
        return stravaService.disconnectStrava();
    }
    @GetMapping(value = {"/status"})
    public ConnectStravaResponse status() {
        return stravaService.statusStrava();
    }
}
