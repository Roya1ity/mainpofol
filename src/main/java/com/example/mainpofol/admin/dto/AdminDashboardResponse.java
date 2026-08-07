package com.example.mainpofol.admin.dto;

import java.util.List;

public record AdminDashboardResponse(
        long visitCount,
        List<VisitCountResponse> dailyVisits,
        List<VisitCountResponse> hourlyVisits
) {
}
