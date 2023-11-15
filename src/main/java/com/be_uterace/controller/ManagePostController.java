package com.be_uterace.controller;


import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ManagePostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-news")
public class ManagePostController {

    private ManagePostService managePostService;

    public ManagePostController(ManagePostService managePostService) {
        this.managePostService = managePostService;
    }

    @PostMapping("/lock/{post_id}")
    public ResponseEntity<ResponseObject> lockPost(@PathVariable Integer post_id){
        ResponseObject responseObject = managePostService.lockPost(post_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/unlock/{post_id}")
    public ResponseEntity<ResponseObject> unlockPost(@PathVariable Integer post_id){
        ResponseObject responseObject = managePostService.unlockPost(post_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/outstanding/{post_id}")
    public ResponseEntity<ResponseObject> outstanding(@PathVariable Integer post_id){
        ResponseObject responseObject = managePostService.outstandingPost(post_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/not-outstanding/{post_id}")
    public ResponseEntity<ResponseObject> notOutstanding(@PathVariable Integer post_id){
        ResponseObject responseObject = managePostService.notOutstandingPost(post_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
}
