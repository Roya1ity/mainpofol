package com.example.mainpofol.myinfo.repository;

import com.example.mainpofol.global.entity.MyInfoAskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyInfoAskHistoryRepository extends JpaRepository<MyInfoAskHistory, Long> {
}
