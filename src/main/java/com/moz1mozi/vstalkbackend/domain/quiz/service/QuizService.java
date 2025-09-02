package com.moz1mozi.vstalkbackend.domain.quiz.service;

import com.moz1mozi.vstalkbackend.domain.post.entity.Category;
import com.moz1mozi.vstalkbackend.domain.quiz.entity.Quiz;
import com.moz1mozi.vstalkbackend.domain.quiz.entity.QuizHistory;
import com.moz1mozi.vstalkbackend.domain.quiz.entity.QuizOption;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import com.moz1mozi.vstalkbackend.domain.quiz.dto.request.QuizAnswerDto;
import com.moz1mozi.vstalkbackend.domain.quiz.dto.request.QuizCreateDto;
import com.moz1mozi.vstalkbackend.domain.quiz.dto.response.QuizDto;
import com.moz1mozi.vstalkbackend.domain.quiz.dto.response.QuizResultDto;
import com.moz1mozi.vstalkbackend.domain.quiz.repository.QuizHistoryRepository;
import com.moz1mozi.vstalkbackend.domain.quiz.repository.QuizRepository;
import com.moz1mozi.vstalkbackend.domain.post.service.CategoryService;
import com.moz1mozi.vstalkbackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final QuizHistoryRepository quizHistoryRepository;

    private final CategoryService categoryService;
    private final QuizOptionService quizOptionService;
    private final UserService userService;

    private final Random random = new Random();

    @Transactional(readOnly = true)
    public QuizDto getQuiz(Long categoryId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<Quiz> quizzes = quizRepository.findUnattemptedActiveByCategoryIdWithOptions(categoryId, user.getId());

        if (quizzes.isEmpty()) {
            throw new IllegalArgumentException("해당 카테고리에 활성화된 퀴즈가 없습니다. categoryId: " + categoryId);
        }

        Quiz quiz = quizzes.get(random.nextInt(quizzes.size()));
        return quiz.toDto();
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

    public QuizResultDto handleAnswer(QuizAnswerDto quizAnswerDto, String username) {
        User user = userService.findByUsername(username);
        Quiz quiz = quizRepository.findById(quizAnswerDto.getQuizId())
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

        QuizOption selectedOption = quiz.getOptions().stream()
                .filter(option -> option.getId().equals(quizAnswerDto.getOptionId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("선택지를 찾을 수 없습니다."));

        boolean isCorrect = selectedOption.isCorrect();

        QuizHistory history = QuizHistory.createFromAnswer(user, quiz, selectedOption);

        quizHistoryRepository.save(history);

        return QuizResultDto.builder()
                .isCorrect(isCorrect)
                .correctOptionId(quiz.getCorrectOption().getId())
                .selectedOptionId(selectedOption.getId())
                .build();
    }
}
