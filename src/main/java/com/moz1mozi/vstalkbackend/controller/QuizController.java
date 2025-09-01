package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.ApiResponse;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizAnswerDto;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizDto;
import com.moz1mozi.vstalkbackend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/post/{categoryId}")
    public ApiResponse<QuizDto> getQuizByPostId(@PathVariable Long categoryId) {
        QuizDto quiz = quizService.getQuiz(categoryId);
        return ApiResponse.ok(quiz);
    }

    @PostMapping("/answer")
    public ApiResponse<?> handleAnswer(@RequestBody QuizAnswerDto quizAnswerDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.ok(quizService.handleAnswer(quizAnswerDto, username));
    }


}
