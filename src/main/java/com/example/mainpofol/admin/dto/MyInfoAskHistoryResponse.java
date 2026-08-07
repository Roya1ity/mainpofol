package com.example.mainpofol.admin.dto;

import com.example.mainpofol.global.entity.MyInfoAskHistory;
import java.time.LocalDateTime;
import java.util.List;

public record MyInfoAskHistoryResponse(
        Long id,
        String question,
        String answer,
        List<String> selectedDocuments,
        LocalDateTime createdAt
) {

    public static MyInfoAskHistoryResponse from(MyInfoAskHistory history) {
        return new MyInfoAskHistoryResponse(
                history.getId(),
                history.getQuestion(),
                history.getAnswer(),
                history.getSelectedDocuments(),
                history.getCreatedAt()
        );
    }
}
