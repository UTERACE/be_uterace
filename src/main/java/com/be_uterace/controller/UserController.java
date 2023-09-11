package com.be_uterace.controller;

import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/a"})
    public ResponseEntity<UserResponse> getUserInfoController(Authentication authentication) {
        UserResponse userResponse = userService.getUserInfo(authentication);
        return ResponseEntity.ok(userResponse);
    }

}
