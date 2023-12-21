package com.be_uterace.controller;

import com.be_uterace.payload.response.RecentActiveResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ReInitializeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/re-initialize")
public class ReInitializeController {

    private ReInitializeService reInitializeService;

    public ReInitializeController(ReInitializeService reInitializeService) {
        this.reInitializeService = reInitializeService;
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> reInitializeAllController() throws InterruptedException, IOException {
        ResponseObject responseObject = reInitializeService.reInitializeAll();
        return ResponseEntity.ok(responseObject);
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<ResponseObject> reInitializeForUserController(
            @PathVariable Long user_id) throws InterruptedException {
        ResponseObject responseObject = reInitializeService.reInitializeForUser(user_id);
        return ResponseEntity.ok(responseObject);
    }
}
