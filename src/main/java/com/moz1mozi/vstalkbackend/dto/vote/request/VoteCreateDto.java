package com.moz1mozi.vstalkbackend.dto.vote.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteCreateDto {

    private String username;
    private Long postId;
    private Long optionId;

    @Builder
    public VoteCreateDto(String username, Long postId, Long optionId) {
        this.username = username;
        this.postId = postId;
        this.optionId = optionId;
    }
}
