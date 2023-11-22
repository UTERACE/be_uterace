package com.be_uterace.controller;

import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ManageUserInitializeResponse;
import com.be_uterace.payload.response.ManageUserStatusResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ManagePostService;
import com.be_uterace.service.ManageUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-user")
public class ManageUserController {
    private ManageUserService manageUserService;

    public ManageUserController(ManageUserService manageUserService) {
        this.manageUserService = manageUserService;
    }

    @PostMapping("/lock/{user_id}")
    public ResponseEntity<ResponseObject> lockUser(@PathVariable Integer user_id, @RequestBody LockRequest lockRequest){
        ResponseObject responseObject = manageUserService.lockUser(user_id, lockRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/unlock/{user_id}")
    public ResponseEntity<ResponseObject> unlockUser(@PathVariable Integer user_id){
        ResponseObject responseObject = manageUserService.unlockUser(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @GetMapping()
    public ResponseEntity<ManageUserStatusResponse> findAllUserStatus(
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam(required=false) String search_name) {
        ManageUserStatusResponse manageUserStatusResponse = manageUserService.findAllUserStatus(current_page,per_page,search_name);
        return ResponseEntity.status(HttpStatus.OK).body(manageUserStatusResponse);
    }

    @GetMapping("/initialize")
    public ResponseEntity<ManageUserInitializeResponse> findAllUserInitialize(
            @RequestParam int current_page,
            @RequestParam int per_page,
            @RequestParam(required=false) String search_name) {
        ManageUserInitializeResponse manageUserInitializeResponse = manageUserService.findAllUserInitialize(current_page,per_page,search_name);
        return ResponseEntity.status(HttpStatus.OK).body(manageUserInitializeResponse);
    }

}
