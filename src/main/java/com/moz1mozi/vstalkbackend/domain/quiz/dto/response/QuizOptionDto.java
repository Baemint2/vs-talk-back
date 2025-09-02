package com.moz1mozi.vstalkbackend.domain.quiz.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class QuizOptionDto {

    private Long id;
    private String optionText;
    private boolean isCorrect;
    private Integer displayOrder;
}
