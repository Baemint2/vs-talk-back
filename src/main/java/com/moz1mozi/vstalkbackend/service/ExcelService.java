package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.quiz.QuizCreateDto;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizOptionCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

    private final PostService postService;
    private final CategoryService categoryService;
    private final QuizService quizService;

    public void process(MultipartFile file, String type, String username) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 헤더 스킵

                switch (type) {
                    case "vote" : createVote(username, row);
                    case "quiz" : createQuiz(row);
                    default:
                        throw new IllegalArgumentException("지원하지 않는 type입니다. : " + type);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("엑셀 처리 실패", e);
        }
    }

    private void createQuiz(Row row) {
        String categoryName = row.getCell(0).getStringCellValue();
        Category category = categoryService.getCategoryByName(categoryName);
        String title = row.getCell(1).getStringCellValue();
        List<QuizOptionCreateDto> quizOptions = new ArrayList<>();
        String answer = row.getCell(6).getStringCellValue();
        for (int i = 1; i < row.getLastCellNum(); i++) {
            String option = row.getCell(i).getStringCellValue();
            QuizOptionCreateDto quizOption = QuizOptionCreateDto.builder()
                    .optionText(option)
                    .isCorrect(option.equals(answer))
                    .displayOrder(i)
                    .build();
            quizOptions.add(quizOption);
        }

        QuizCreateDto quizCreateDto = QuizCreateDto.builder()
                .title(title)
                .categoryId(category.getId())
                .options(quizOptions)
                .build();
        quizService.createQuiz(quizCreateDto);
    }

    private void createVote(String username, Row row) {
        // 카테고리
        // 투표 종료는 default로 넣기
        // 이미지, 유튜브 링크?
        // 투표 옵션
        String title = row.getCell(0).getStringCellValue();
        String categoryName = row.getCell(1).getStringCellValue();
        Category category = categoryService.getCategoryByName(categoryName);
        //
        List<VoteOptionCreateDto> voteOptions = new ArrayList<>();
        for (int i = 2; i < row.getLastCellNum(); i++) {
            String stringCellValue = row.getCell(i).getStringCellValue();
            VoteOptionCreateDto voteOption = VoteOptionCreateDto.builder()
                    .optionText(stringCellValue).build();
            voteOptions.add(voteOption);
        }

        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title(title)
                .categoryId(category.getId())
                .voteOptions(voteOptions)
                .voteEndTime(now().plusWeeks(1))
                .isSecret(false)
                .voteEnabled(true)
                .build();

        postService.createPost(postCreateDto, username);
    }
}
