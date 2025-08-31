package com.moz1mozi.vstalkbackend.dto.quiz;

import lombok.Getter;

@Getter
public class QuizResultDto {
    private boolean isCorrect;
    private String selectedOptionText;
    private String correctOptionText;
}
