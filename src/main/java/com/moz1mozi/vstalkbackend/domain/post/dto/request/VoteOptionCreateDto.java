package com.moz1mozi.vstalkbackend.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteOptionCreateDto {
    private Long id;
    private String optionText;
    private String color;

    @Builder
    public VoteOptionCreateDto(String optionText, String color) {
        this.optionText = optionText;
        this.color = color;
    }
}
