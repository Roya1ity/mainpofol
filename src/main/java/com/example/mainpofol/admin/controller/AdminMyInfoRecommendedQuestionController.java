package com.example.mainpofol.admin.controller;

import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionCreateRequest;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionResponse;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionUpdateRequest;
import com.example.mainpofol.admin.service.AdminMyInfoRecommendedQuestionService;
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
@RequestMapping("/api/admin/recommended-questions")
public class AdminMyInfoRecommendedQuestionController {

    private final AdminMyInfoRecommendedQuestionService recommendedQuestionService;

    @GetMapping
    public Page<MyInfoRecommendedQuestionResponse> findAll(@PageableDefault(size = 20) Pageable pageable) {
        return recommendedQuestionService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public MyInfoRecommendedQuestionResponse findById(@PathVariable Long id) {
        return recommendedQuestionService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MyInfoRecommendedQuestionResponse create(@Valid @RequestBody MyInfoRecommendedQuestionCreateRequest request) {
        return recommendedQuestionService.create(request);
    }

    @PutMapping("/{id}")
    public MyInfoRecommendedQuestionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody MyInfoRecommendedQuestionUpdateRequest request
    ) {
        return recommendedQuestionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        recommendedQuestionService.delete(id);
    }
}
