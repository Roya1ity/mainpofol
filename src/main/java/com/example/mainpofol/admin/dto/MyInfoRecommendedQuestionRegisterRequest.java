package com.example.mainpofol.admin.dto;

public record MyInfoRecommendedQuestionRegisterRequest(
        String question,
        Boolean enabled,
        Integer displayOrder
) {
}
