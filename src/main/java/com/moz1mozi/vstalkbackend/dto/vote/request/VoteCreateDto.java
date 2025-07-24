package com.moz1mozi.vstalkbackend.dto.vote.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteCreateDto {

    private String username;
    private Long postId;
    private Long optionId;
}
