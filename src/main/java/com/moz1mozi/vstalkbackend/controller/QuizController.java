package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.ApiResponse;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizDto;
import com.moz1mozi.vstalkbackend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
