package com.example.mainpofol.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record MyInfoDocumentUpdateRequest(
        @NotBlank String content
) {
}
