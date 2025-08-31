package com.moz1mozi.vstalkbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizHistory extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_option_id")
    private QuizOption selectedOption;

    private boolean isCorrect;      // 정답 여부
    private LocalDateTime completedAt;  // 완료 시간

    @Builder
    public QuizHistory(User user, Quiz quiz, QuizOption selectedOption,
                       boolean isCorrect, LocalDateTime completedAt) {
        this.user = user;
        this.quiz = quiz;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.completedAt = completedAt;
    }

    // Quiz를 통해 Category에 접근
    public Category getCategory() {
        return this.quiz != null ? this.quiz.getCategory() : null;
    }

}
