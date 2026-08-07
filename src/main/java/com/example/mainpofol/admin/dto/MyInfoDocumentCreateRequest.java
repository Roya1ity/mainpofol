package com.example.mainpofol.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record MyInfoDocumentCreateRequest(
        @NotBlank String fileName,
        @NotBlank String content
) {
}
