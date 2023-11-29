package com.be_uterace.controller;

import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ResponseObject> changePasswordController(@RequestBody @Valid ChangePasswordDto changePasswordDto, Authentication authentication) {
        ResponseObject userResponse = userService.changePassword(changePasswordDto,authentication);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping(value = {"/update"})
    public ResponseEntity<ResponseObject> updateUserController(@RequestBody UpdateDto updateDto, Authentication authentication) {
        ResponseObject userResponse = userService.updateUser(updateDto,authentication);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/recent-active/{user_id}")
    public ResponseEntity<RecentActiveResponse> recentActiveController(
            @PathVariable Long user_id,
            @RequestParam (defaultValue = "1" )int current_page,
            @RequestParam (defaultValue = "5" ) int per_page,
            @RequestParam(required = false) String search_name,
            @RequestParam(defaultValue = "48") int hour) {

        RecentActiveResponse recentActiveResponse = userService.getRecentActivity(
                current_page,per_page,user_id,search_name,hour);

        return ResponseEntity.ok(recentActiveResponse);
    }

    @GetMapping("/recent-active")
    public ResponseEntity<RecentActiveResponse> recentActiveOwnController(
            @RequestParam (defaultValue = "1" )int current_page,
            @RequestParam (defaultValue = "5" ) int per_page,
            @RequestParam(required = false) String search_name,
            @RequestParam(defaultValue = "48") int hour,
            Authentication authentication) {

        RecentActiveResponse recentActiveResponse = userService.getRecentActivity(
                current_page,per_page,search_name,hour,authentication);

        return ResponseEntity.ok(recentActiveResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserStatisticResponse> UserStatisticOwnController() {
        UserStatisticResponse userStatisticResponse = userService.getSummaryActivity(null);
        return ResponseEntity.ok(userStatisticResponse);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{user_id}")
    public ResponseEntity<UserStatisticResponse> UserStatisticController(@PathVariable Long user_id) {
        UserStatisticResponse userStatisticResponse = userService.getSummaryActivity(user_id);
        return ResponseEntity.ok(userStatisticResponse);
    }

}
