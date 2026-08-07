package com.example.mainpofol.myinfo.service;

import com.example.mainpofol.admin.repository.MyInfoRecommendedQuestionRepository;
import com.example.mainpofol.myinfo.dto.MyInfoRecommendedAnswerResponse;
import com.example.mainpofol.myinfo.dto.MyInfoRecommendedQuestionListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MyInfoRecommendedQuestionService {

    private final MyInfoRecommendedQuestionRepository recommendedQuestionRepository;

    @Transactional(readOnly = true)
    public List<MyInfoRecommendedQuestionListResponse> findAllEnabled() {
        return recommendedQuestionRepository.findAllByEnabledTrueOrderByDisplayOrderAscIdAsc()
                .stream()
                .map(MyInfoRecommendedQuestionListResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MyInfoRecommendedAnswerResponse findAnswerByRecommendedQuestionId(Long id) {
        return recommendedQuestionRepository.findByIdAndEnabledTrue(id)
                .map(MyInfoRecommendedAnswerResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommended question not found."));
    }
}
