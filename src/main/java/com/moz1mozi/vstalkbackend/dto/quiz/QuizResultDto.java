package com.moz1mozi.vstalkbackend.dto.quiz;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizResultDto {
    private boolean isCorrect;
    private Long selectedOptionId;
    private Long correctOptionId;
}
