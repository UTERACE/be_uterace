package com.be_uterace.controller;

import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.RegisterDto;
import com.be_uterace.payload.response.JWTAuthResponse;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private UserRepository userRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){
        LoginResponse loginResponse = authService.login(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<ResponseObject> register(@RequestBody RegisterDto registerDto){
        ResponseObject loginResponse = authService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}
