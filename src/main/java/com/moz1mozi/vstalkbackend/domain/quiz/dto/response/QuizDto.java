package com.moz1mozi.vstalkbackend.domain.quiz.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class QuizDto {
    private Long id;
    private String title;
    private Long categoryId;
    private List<QuizOptionDto> options;
}
