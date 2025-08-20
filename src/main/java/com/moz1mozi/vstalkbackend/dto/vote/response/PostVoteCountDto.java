package com.moz1mozi.vstalkbackend.dto.vote.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostVoteCountDto {
    private Long postId;
    private Long count;

    public PostVoteCountDto(Long postId, Long count) {
        this.postId = postId;
        this.count = count;
    }
}
