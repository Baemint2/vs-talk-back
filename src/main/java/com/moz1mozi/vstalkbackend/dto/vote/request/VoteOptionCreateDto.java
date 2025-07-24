package com.moz1mozi.vstalkbackend.dto.vote.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteOptionCreateDto {
    private String optionText;
    private String color;
}
