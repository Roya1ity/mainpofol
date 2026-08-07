package com.example.mainpofol.myinfo.dto;

import com.example.mainpofol.global.entity.MyInfoRecommendedQuestion;
import java.time.LocalDateTime;
import java.util.List;

public record MyInfoRecommendedAnswerResponse(
        Long recommendedQuestionId,
        Long askHistoryId,
        String question,
        String answer,
        List<String> selectedDocuments,
        LocalDateTime createdAt
) {

    public static MyInfoRecommendedAnswerResponse from(MyInfoRecommendedQuestion recommendedQuestion) {
        return new MyInfoRecommendedAnswerResponse(
                recommendedQuestion.getId(),
                recommendedQuestion.getAskHistory().getId(),
                recommendedQuestion.getAskHistory().getQuestion(),
                recommendedQuestion.getAskHistory().getAnswer(),
                recommendedQuestion.getAskHistory().getSelectedDocuments(),
                recommendedQuestion.getAskHistory().getCreatedAt()
        );
    }
}
