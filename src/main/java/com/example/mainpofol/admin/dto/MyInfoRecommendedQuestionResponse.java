package com.example.mainpofol.admin.dto;

import com.example.mainpofol.global.entity.MyInfoRecommendedQuestion;
import java.time.LocalDateTime;

public record MyInfoRecommendedQuestionResponse(
        Long id,
        String question,
        Long askHistoryId,
        String askHistoryQuestion,
        Boolean enabled,
        Integer displayOrder,
        LocalDateTime createdAt
) {

    public static MyInfoRecommendedQuestionResponse from(MyInfoRecommendedQuestion recommendedQuestion) {
        return new MyInfoRecommendedQuestionResponse(
                recommendedQuestion.getId(),
                recommendedQuestion.getQuestion(),
                recommendedQuestion.getAskHistory().getId(),
                recommendedQuestion.getAskHistory().getQuestion(),
                recommendedQuestion.getEnabled(),
                recommendedQuestion.getDisplayOrder(),
                recommendedQuestion.getCreatedAt()
        );
    }
}
