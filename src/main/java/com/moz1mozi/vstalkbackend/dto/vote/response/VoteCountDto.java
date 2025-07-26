package com.moz1mozi.vstalkbackend.dto.vote.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VoteCountDto {
    private Long voteOptionId;
    private Long count;
    private Long totalCount;

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
