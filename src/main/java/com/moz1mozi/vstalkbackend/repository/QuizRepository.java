package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 여러 개 조회 후 랜덤 선택용
    @Query("SELECT q FROM Quiz q " +
            "JOIN FETCH q.options " +
            "WHERE q.category.id = :categoryId " +
            "AND q.isActive = true " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM QuizHistory qh " +
            "WHERE qh.quiz.id = q.id " +
            "AND qh.user.id = :userId" +
            ")")
    List<Quiz> findUnattemptedActiveByCategoryIdWithOptions(@Param("categoryId") Long categoryId,
                                                            @Param("userId") Long userId);
}
