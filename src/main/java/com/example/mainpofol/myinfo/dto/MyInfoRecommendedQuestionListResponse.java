package com.example.mainpofol.myinfo.dto;

import com.example.mainpofol.global.entity.MyInfoRecommendedQuestion;

public record MyInfoRecommendedQuestionListResponse(
        Long id,
        String question,
        Integer displayOrder
) {

    public static MyInfoRecommendedQuestionListResponse from(MyInfoRecommendedQuestion recommendedQuestion) {
        return new MyInfoRecommendedQuestionListResponse(
                recommendedQuestion.getId(),
                recommendedQuestion.getQuestion(),
                recommendedQuestion.getDisplayOrder()
        );
    }
}
