package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.quiz.QuizCreateDto;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizDto;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizOptionDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.Quiz;
import com.moz1mozi.vstalkbackend.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {

    private final QuizRepository quizRepository;
    private final Random random = new Random();
    private final CategoryService categoryService;
    private final QuizOptionService quizOptionService;

    @Transactional(readOnly = true)
    public QuizDto getQuiz(Long categoryId) {
        List<Quiz> quizzes = quizRepository.findAllActiveByCategoryIdWithOptions(categoryId);

        if (quizzes.isEmpty()) {
            throw new IllegalArgumentException("해당 카테고리에 활성화된 퀴즈가 없습니다. categoryId: " + categoryId);
        }

        Quiz quiz = quizzes.get(random.nextInt(quizzes.size()));
        return convertToDto(quiz);

    }

    private static QuizDto convertToDto(Quiz quiz) {
        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .categoryId(quiz.getCategory().getId())
                .options(quiz.getOptions().stream()
                        .map(option -> QuizOptionDto.builder()
                                .id(option.getId())
                                .optionText(option.getOptionText())
                                .isCorrect(option.isCorrect())
                                .displayOrder(option.getDisplayOrder())
                                .build())
                        .toList())
                .build();
    }

    public void createQuiz(QuizCreateDto quizCreateDto) {
        Category category = categoryService.getCategory(quizCreateDto.getCategoryId());
        Quiz quiz = Quiz.builder()
                .title(quizCreateDto.getTitle())
                .category(category)
                .isActive(true)
                .build();
        Quiz savedQuiz = quizRepository.save(quiz);

        quizOptionService.createQuizOptions(savedQuiz, quizCreateDto.getOptions());
    }
}
