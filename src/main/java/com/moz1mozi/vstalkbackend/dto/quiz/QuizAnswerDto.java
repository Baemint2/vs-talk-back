package com.moz1mozi.vstalkbackend.dto.quiz;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizAnswerDto {

    private Long quizId;
    private Long optionId;
}
