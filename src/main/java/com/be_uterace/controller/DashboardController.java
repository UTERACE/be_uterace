package com.be_uterace.controller;

import com.be_uterace.payload.response.DashboardResponse;
import com.be_uterace.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping()
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }
}
