package com.moz1mozi.vstalkbackend.dto.vote.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteCreateDto {

    private Long postId;
    private Long optionId;

    @Builder
    public VoteCreateDto(Long postId, Long optionId) {
        this.postId = postId;
        this.optionId = optionId;
    }
}
