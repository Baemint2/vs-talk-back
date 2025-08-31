package com.moz1mozi.vstalkbackend.dto.quiz;

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
