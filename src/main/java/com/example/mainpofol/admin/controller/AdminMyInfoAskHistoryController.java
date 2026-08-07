package com.example.mainpofol.admin.controller;

import com.example.mainpofol.admin.dto.MyInfoAskHistoryCreateRequest;
import com.example.mainpofol.admin.dto.MyInfoAskHistoryResponse;
import com.example.mainpofol.admin.dto.MyInfoAskHistoryUpdateRequest;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionRegisterRequest;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionResponse;
import com.example.mainpofol.admin.service.AdminMyInfoAskHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/myinfo-ask-histories")
public class AdminMyInfoAskHistoryController {

    private final AdminMyInfoAskHistoryService askHistoryService;

    @GetMapping
    public Page<MyInfoAskHistoryResponse> findAll(@PageableDefault(size = 20) Pageable pageable) {
        return askHistoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public MyInfoAskHistoryResponse findById(@PathVariable Long id) {
        return askHistoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MyInfoAskHistoryResponse create(@Valid @RequestBody MyInfoAskHistoryCreateRequest request) {
        return askHistoryService.create(request);
    }

    @PutMapping("/{id}")
    public MyInfoAskHistoryResponse update(
            @PathVariable Long id,
            @Valid @RequestBody MyInfoAskHistoryUpdateRequest request
    ) {
        return askHistoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        askHistoryService.delete(id);
    }

    @PostMapping("/{id}/recommended-question")
    @ResponseStatus(HttpStatus.CREATED)
    public MyInfoRecommendedQuestionResponse registerRecommendedQuestion(
            @PathVariable Long id,
            @RequestBody(required = false) MyInfoRecommendedQuestionRegisterRequest request
    ) {
        return askHistoryService.registerRecommendedQuestion(id, request);
    }
}
