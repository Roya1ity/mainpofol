package com.example.mainpofol.admin.controller;

import com.example.mainpofol.admin.dto.AdminDashboardResponse;
import com.example.mainpofol.myinfo.service.SiteVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final SiteVisitService siteVisitService;

    @GetMapping
    public AdminDashboardResponse findDashboard() {
        return new AdminDashboardResponse(
                siteVisitService.count(),
                siteVisitService.countDailyVisits(),
                siteVisitService.countHourlyVisitsToday()
        );
    }
}
