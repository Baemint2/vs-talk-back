package com.moz1mozi.vstalkbackend.entity;

import com.moz1mozi.vstalkbackend.dto.quiz.QuizDto;
import com.moz1mozi.vstalkbackend.dto.quiz.QuizOptionDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseTimeEntity {

    @Id @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // OneToMany: 하나의 퀴즈가 여러 옵션을 가짐
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizOption> options = new ArrayList<>();


    @Builder
    public Quiz(String title, boolean isActive, Category category, List<QuizOption> options) {
        this.title = title;
        this.isActive = isActive;
        this.category = category;
        this.options = options;
    }

    public void addOption(QuizOption option) {
        this.options.add(option);
    }

    public void removeOption(QuizOption option) {
        this.options.remove(option);
    }

    public QuizOption getCorrectOption() {
        return options.stream()
                .filter(QuizOption::isCorrect)
                .findFirst()
                .orElse(null);
    }

    public void updateActiveStatus(boolean isActive) {
        this.isActive = isActive;
    }


    public QuizDto toDto() {
        return QuizDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .categoryId(this.getCategory().getId())
                .options(this.getOptions().stream()
                        .map(option -> QuizOptionDto.builder()
                                .id(option.getId())
                                .optionText(option.getOptionText())
                                .isCorrect(option.isCorrect())
                                .displayOrder(option.getDisplayOrder())
                                .build())
                        .toList())
                .build();
    }

}
