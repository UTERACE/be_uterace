package com.be_uterace.controller;

import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/strava")
public class StravaController {

    @PostMapping(value = {"/connect"})
    public ResponseEntity<LoginResponse> connect(@RequestBody LoginDto loginDto){

        return null;
    }
}
