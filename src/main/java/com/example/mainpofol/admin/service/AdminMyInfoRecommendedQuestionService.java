package com.example.mainpofol.admin.service;

import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionCreateRequest;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionResponse;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionUpdateRequest;
import com.example.mainpofol.admin.repository.MyInfoRecommendedQuestionRepository;
import com.example.mainpofol.global.entity.MyInfoAskHistory;
import com.example.mainpofol.global.entity.MyInfoRecommendedQuestion;
import com.example.mainpofol.myinfo.repository.MyInfoAskHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminMyInfoRecommendedQuestionService {

    private final MyInfoRecommendedQuestionRepository recommendedQuestionRepository;
    private final MyInfoAskHistoryRepository askHistoryRepository;

    @Transactional(readOnly = true)
    public Page<MyInfoRecommendedQuestionResponse> findAll(Pageable pageable) {
        return recommendedQuestionRepository.findAll(pageable)
                .map(MyInfoRecommendedQuestionResponse::from);
    }

    @Transactional(readOnly = true)
    public MyInfoRecommendedQuestionResponse findById(Long id) {
        return MyInfoRecommendedQuestionResponse.from(getRecommendedQuestion(id));
    }

    @Transactional
    public MyInfoRecommendedQuestionResponse create(MyInfoRecommendedQuestionCreateRequest request) {
        validateNotLinked(request.askHistoryId());
        MyInfoAskHistory askHistory = getAskHistory(request.askHistoryId());

        MyInfoRecommendedQuestion recommendedQuestion = recommendedQuestionRepository.save(MyInfoRecommendedQuestion.builder()
                .question(request.question())
                .askHistory(askHistory)
                .enabled(request.enabled() == null || request.enabled())
                .displayOrder(request.displayOrder() == null ? 0 : request.displayOrder())
                .build());

        return MyInfoRecommendedQuestionResponse.from(recommendedQuestion);
    }

    @Transactional
    public MyInfoRecommendedQuestionResponse update(Long id, MyInfoRecommendedQuestionUpdateRequest request) {
        MyInfoRecommendedQuestion recommendedQuestion = getRecommendedQuestion(id);
        validateNotLinkedToOther(request.askHistoryId(), id);
        MyInfoAskHistory askHistory = getAskHistory(request.askHistoryId());

        recommendedQuestion.update(
                request.question(),
                askHistory,
                request.enabled() == null || request.enabled(),
                request.displayOrder() == null ? 0 : request.displayOrder()
        );

        return MyInfoRecommendedQuestionResponse.from(recommendedQuestion);
    }

    @Transactional
    public void delete(Long id) {
        MyInfoRecommendedQuestion recommendedQuestion = getRecommendedQuestion(id);
        recommendedQuestionRepository.delete(recommendedQuestion);
    }

    private MyInfoRecommendedQuestion getRecommendedQuestion(Long id) {
        return recommendedQuestionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommended question not found."));
    }

    private MyInfoAskHistory getAskHistory(Long id) {
        return askHistoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ask history not found."));
    }

    private void validateNotLinked(Long askHistoryId) {
        if (recommendedQuestionRepository.existsByAskHistoryId(askHistoryId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ask history is already linked to a recommended question.");
        }
    }

    private void validateNotLinkedToOther(Long askHistoryId, Long id) {
        if (recommendedQuestionRepository.existsByAskHistoryIdAndIdNot(askHistoryId, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ask history is already linked to another recommended question.");
        }
    }
}
