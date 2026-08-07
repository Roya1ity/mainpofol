package com.example.mainpofol.myinfo.controller;

import com.example.mainpofol.myinfo.dto.MyInfoRecommendedAnswerResponse;
import com.example.mainpofol.myinfo.dto.MyInfoRecommendedQuestionListResponse;
import com.example.mainpofol.myinfo.service.MyInfoRecommendedQuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myinfo/recommended-questions")
public class MyInfoRecommendedQuestionController {

    private final MyInfoRecommendedQuestionService recommendedQuestionService;

    @GetMapping
    public List<MyInfoRecommendedQuestionListResponse> findAll() {
        return recommendedQuestionService.findAllEnabled();
    }

    @GetMapping("/{id}/answer")
    public MyInfoRecommendedAnswerResponse findAnswer(@PathVariable Long id) {
        return recommendedQuestionService.findAnswerByRecommendedQuestionId(id);
    }
}
