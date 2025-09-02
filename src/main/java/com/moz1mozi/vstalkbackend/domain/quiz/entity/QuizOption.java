package com.moz1mozi.vstalkbackend.domain.quiz.entity;

import com.moz1mozi.vstalkbackend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizOption extends BaseTimeEntity {

    @Id @Column(name = "quiz_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionText;    // 선택지 내용
    private boolean isCorrect;    // 정답 여부
    private Integer displayOrder;      // 선택지 순서

    // ManyToOne: 여러 옵션이 하나의 퀴즈에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Builder
    public QuizOption(String optionText, boolean isCorrect, int displayOrder, Quiz quiz) {
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.displayOrder = displayOrder;
        this.quiz = quiz;
    }


}
