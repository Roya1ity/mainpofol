package com.example.mainpofol.myinfo.service;

import com.example.mainpofol.admin.dto.VisitCountResponse;
import com.example.mainpofol.global.entity.SiteVisit;
import com.example.mainpofol.myinfo.repository.SiteVisitRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteVisitService {

    private final SiteVisitRepository siteVisitRepository;

    @Transactional
    public void record() {
        siteVisitRepository.save(SiteVisit.builder()
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional(readOnly = true)
    public long count() {
        return siteVisitRepository.count();
    }

    @Transactional(readOnly = true)
    public List<VisitCountResponse> countDailyVisits() {
        return siteVisitRepository.countDailyVisits()
                .stream()
                .map(row -> new VisitCountResponse(row.getBucket(), row.getVisitCount()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VisitCountResponse> countHourlyVisitsToday() {
        return siteVisitRepository.countHourlyVisitsToday()
                .stream()
                .map(row -> new VisitCountResponse(row.getBucket() + "시", row.getVisitCount()))
                .toList();
    }
}
