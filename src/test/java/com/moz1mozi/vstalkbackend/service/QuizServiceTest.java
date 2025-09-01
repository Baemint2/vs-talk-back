package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.quiz.QuizDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @DisplayName("")
    @Test
    void test() {
        // given // when
        QuizDto quiz = quizService.getQuiz(39L);
        System.out.println(quiz);
        // then
    }
}