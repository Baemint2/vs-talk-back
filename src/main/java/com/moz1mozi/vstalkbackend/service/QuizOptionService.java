package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.quiz.QuizOptionCreateDto;
import com.moz1mozi.vstalkbackend.entity.Quiz;
import com.moz1mozi.vstalkbackend.entity.QuizOption;
import com.moz1mozi.vstalkbackend.repository.QuizOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizOptionService {

    private final QuizOptionRepository quizOptionRepository;

    public void createQuizOptions(Quiz quiz, List<QuizOptionCreateDto> options) {

        List<QuizOption> quizOptions = options.stream()
                .map(option -> QuizOption.builder()
                        .quiz(quiz)
                        .optionText(option.getOptionText())
                        .isCorrect(option.isCorrect())
                        .displayOrder(option.getDisplayOrder())
                        .build())
                .toList();

        quizOptionRepository.saveAll(quizOptions);
    }
}
