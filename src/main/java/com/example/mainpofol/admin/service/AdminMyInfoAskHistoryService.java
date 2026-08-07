package com.example.mainpofol.admin.service;

import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionRegisterRequest;
import com.example.mainpofol.admin.dto.MyInfoRecommendedQuestionResponse;
import com.example.mainpofol.admin.repository.MyInfoRecommendedQuestionRepository;
import com.example.mainpofol.admin.dto.MyInfoAskHistoryCreateRequest;
import com.example.mainpofol.admin.dto.MyInfoAskHistoryResponse;
import com.example.mainpofol.admin.dto.MyInfoAskHistoryUpdateRequest;
import com.example.mainpofol.global.entity.MyInfoAskHistory;
import com.example.mainpofol.global.entity.MyInfoRecommendedQuestion;
import com.example.mainpofol.myinfo.repository.MyInfoAskHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminMyInfoAskHistoryService {

    private final MyInfoAskHistoryRepository repository;
    private final MyInfoRecommendedQuestionRepository recommendedQuestionRepository;

    @Transactional(readOnly = true)
    public Page<MyInfoAskHistoryResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(MyInfoAskHistoryResponse::from);
    }

    @Transactional(readOnly = true)
    public MyInfoAskHistoryResponse findById(Long id) {
        return MyInfoAskHistoryResponse.from(getHistory(id));
    }

    @Transactional
    public MyInfoAskHistoryResponse create(MyInfoAskHistoryCreateRequest request) {
        MyInfoAskHistory history = repository.save(MyInfoAskHistory.builder()
                .question(request.question())
                .answer(request.answer())
                .selectedDocuments(request.selectedDocuments())
                .build());

        return MyInfoAskHistoryResponse.from(history);
    }

    @Transactional
    public MyInfoAskHistoryResponse save(String question, String answer, List<String> selectedDocuments) {
        MyInfoAskHistory history = repository.save(MyInfoAskHistory.builder()
                .question(question)
                .answer(answer)
                .selectedDocuments(selectedDocuments)
                .build());

        return MyInfoAskHistoryResponse.from(history);
    }

    @Transactional
    public MyInfoAskHistoryResponse update(Long id, MyInfoAskHistoryUpdateRequest request) {
        MyInfoAskHistory history = getHistory(id);
        history.update(request.question(), request.answer(), request.selectedDocuments());
        return MyInfoAskHistoryResponse.from(history);
    }

    @Transactional
    public void delete(Long id) {
        MyInfoAskHistory history = getHistory(id);
        repository.delete(history);
    }

    @Transactional
    public MyInfoRecommendedQuestionResponse registerRecommendedQuestion(
            Long id,
            MyInfoRecommendedQuestionRegisterRequest request
    ) {
        if (recommendedQuestionRepository.existsByAskHistoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ask history is already linked to a recommended question.");
        }

        MyInfoAskHistory history = getHistory(id);
        MyInfoRecommendedQuestion recommendedQuestion = recommendedQuestionRepository.save(MyInfoRecommendedQuestion.builder()
                .question(resolveRecommendedQuestionText(request, history))
                .askHistory(history)
                .enabled(request == null || request.enabled() == null || request.enabled())
                .displayOrder(request == null || request.displayOrder() == null ? 0 : request.displayOrder())
                .build());

        return MyInfoRecommendedQuestionResponse.from(recommendedQuestion);
    }

    private MyInfoAskHistory getHistory(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ask history not found."));
    }

    private String resolveRecommendedQuestionText(
            MyInfoRecommendedQuestionRegisterRequest request,
            MyInfoAskHistory history
    ) {
        if (request == null || request.question() == null || request.question().isBlank()) {
            return history.getQuestion();
        }

        return request.question();
    }
}
