package com.moz1mozi.vstalkbackend.dto.post.request;

import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostUpdateDto {

    private Long id;
    private String title;
    private String content;
    private boolean isSecret;
    private boolean isDeleted;
    private Long categoryId;
    private String videoId;
    private List<VoteOptionCreateDto> voteOptions;

    public boolean hasVoteOptions() {
        return voteOptions != null && !voteOptions.isEmpty();
    }
}
