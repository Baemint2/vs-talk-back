package com.moz1mozi.vstalkbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteOptionDto {
    private Long id;
    private String optionText;
    private String color;

    @Builder
    public VoteOptionDto(Long id, String optionText, String color) {
        this.id = id;
        this.optionText = optionText;
        this.color = color;
    }
}
