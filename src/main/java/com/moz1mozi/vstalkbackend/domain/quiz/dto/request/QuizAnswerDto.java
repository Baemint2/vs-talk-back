package com.moz1mozi.vstalkbackend.domain.quiz.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizAnswerDto {

    private Long quizId;
    private Long optionId;
}
