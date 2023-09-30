package com.be_uterace.controller;

import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getUserInfoController(Authentication authentication) {
        UserResponse userResponse = userService.getUserInfo(authentication);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping(value = {"/change-password"})
    public ResponseEntity<ResponseObject> changePasswordController(@RequestBody ChangePasswordDto changePasswordDto, Authentication authentication) {
        ResponseObject userResponse = userService.changePassword(changePasswordDto,authentication);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping(value = {"/update"})
    public ResponseEntity<ResponseObject> updateUserController(@RequestBody UpdateDto updateDto, Authentication authentication) {
        ResponseObject userResponse = userService.updateUser(updateDto,authentication);
        return ResponseEntity.ok(userResponse);
    }

}
