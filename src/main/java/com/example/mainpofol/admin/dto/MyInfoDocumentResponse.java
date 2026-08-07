package com.example.mainpofol.admin.dto;

import java.time.LocalDateTime;

public record MyInfoDocumentResponse(
        String fileName,
        long size,
        LocalDateTime lastModifiedAt,
        String content
) {
}
