package com.example.mainpofol.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MyInfoRecommendedQuestionCreateRequest(
        @NotBlank String question,
        @NotNull Long askHistoryId,
        Boolean enabled,
        Integer displayOrder
) {
}
