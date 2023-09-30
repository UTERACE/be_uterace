package com.be_uterace.controller;

import com.be_uterace.payload.response.HomePageResponse;
import com.be_uterace.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomePageController {
    private HomeService homeService;

    public HomePageController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping(value = {""})
    public ResponseEntity<HomePageResponse> getProvinceController() {
        HomePageResponse homePageResponse = homeService.getHomePage();
        return ResponseEntity.ok(homePageResponse);
    }
}
