package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {

    boolean existsByUserIdAndQuizIdAndIsCorrectTrue(Long userId, Long quizId);

    boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    Optional<QuizHistory> findByUserIdAndQuizId(Long userId, Long quizId);
}
