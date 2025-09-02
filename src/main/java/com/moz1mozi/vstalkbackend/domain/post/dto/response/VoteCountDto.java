package com.moz1mozi.vstalkbackend.domain.post.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VoteCountDto {
    private Long voteOptionId;
    private Long count;

    public VoteCountDto(Long voteOptionId, Long count) {
        this.voteOptionId = voteOptionId;
        this.count = count;
    }

    @Override
    public String toString() {
        return "VoteCountDto{" +
                "voteOptionId=" + voteOptionId +
                ", count=" + count +
                '}';
    }
}
