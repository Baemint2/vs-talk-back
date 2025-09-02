package com.moz1mozi.vstalkbackend.domain.quiz.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuizCreateDto {

    private Long id;
    private String title;
    private Long categoryId;
    private List<QuizOptionCreateDto> options;
}
