package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 여러 개 조회 후 랜덤 선택용
    @Query("SELECT q FROM Quiz q " +
            "JOIN FETCH q.options " +
            "WHERE q.category.id = :categoryId " +
            "AND q.isActive = true")
    List<Quiz> findAllActiveByCategoryIdWithOptions(@Param("categoryId") Long categoryId);

}
