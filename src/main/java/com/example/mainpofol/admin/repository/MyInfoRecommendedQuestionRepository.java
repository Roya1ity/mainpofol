package com.example.mainpofol.admin.repository;

import com.example.mainpofol.global.entity.MyInfoRecommendedQuestion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyInfoRecommendedQuestionRepository extends JpaRepository<MyInfoRecommendedQuestion, Long> {

    boolean existsByAskHistoryId(Long askHistoryId);

    boolean existsByAskHistoryIdAndIdNot(Long askHistoryId, Long id);

    Optional<MyInfoRecommendedQuestion> findByIdAndEnabledTrue(Long id);

    List<MyInfoRecommendedQuestion> findAllByEnabledTrueOrderByDisplayOrderAscIdAsc();
}
