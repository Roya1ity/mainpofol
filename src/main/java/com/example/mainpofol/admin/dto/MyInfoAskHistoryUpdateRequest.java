package com.example.mainpofol.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MyInfoAskHistoryUpdateRequest(
        @NotBlank String question,
        @NotBlank String answer,
        @NotNull List<String> selectedDocuments
) {
}
