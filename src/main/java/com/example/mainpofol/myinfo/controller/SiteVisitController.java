package com.example.mainpofol.myinfo.controller;

import com.example.mainpofol.myinfo.service.SiteVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myinfo/visits")
public class SiteVisitController {

    private final SiteVisitService siteVisitService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void record() {
        siteVisitService.record();
    }
}
