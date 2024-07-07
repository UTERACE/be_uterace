package com.be_uterace.controller;

import com.be_uterace.payload.request.LockRequest;
import com.be_uterace.payload.response.ManageEventSearchResponse;
import com.be_uterace.payload.response.ManagePaymentSearchResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ManageEventService;
import com.be_uterace.service.ManagePaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-payment")
public class ManagePaymentController {
    private final ManagePaymentService managePaymentService;

    public ManagePaymentController(ManagePaymentService managePaymentService) {
        this.managePaymentService = managePaymentService;
    }

    @GetMapping()
    public ResponseEntity<ManagePaymentSearchResponse> findPaymentController(
            @RequestParam (defaultValue = "1") int current_page ,
            @RequestParam (defaultValue = "5") int per_page,
            @RequestParam (required = false) Long transaction_id,
            @RequestParam (defaultValue = "1")int provider_id) {
        if(provider_id==1){
            ManagePaymentSearchResponse managePaymentSearchResponse = managePaymentService.searchMomoPayment(current_page,per_page,null);
            return ResponseEntity.status(HttpStatus.OK).body(managePaymentSearchResponse);
        }else {
            ManagePaymentSearchResponse managePaymentSearchResponse = managePaymentService.searchVNPayPayment(current_page,per_page);
            return ResponseEntity.status(HttpStatus.OK).body(managePaymentSearchResponse);
        }
    }




}
