package com.moz1mozi.vstalkbackend.dto.quiz;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QuizOptionCreateDto {

    private String optionText;
    private boolean isCorrect;
    private int displayOrder;
}
