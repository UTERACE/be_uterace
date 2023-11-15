package com.be_uterace.controller;


import com.be_uterace.payload.request.CreatePostDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ManagePostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-news")
public class ManageNewsController {

    private ManagePostService managePostService;

    public ManageNewsController(ManagePostService managePostService) {
        this.managePostService = managePostService;
    }

    @PostMapping("/block/{post_id}")
    public ResponseEntity<ResponseObject> blockPost(@PathVariable Integer post_id){
        ResponseObject responseObject = managePostService.blockPost(post_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
}
