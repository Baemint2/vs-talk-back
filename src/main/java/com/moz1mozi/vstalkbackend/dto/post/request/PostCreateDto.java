package com.moz1mozi.vstalkbackend.dto.post.request;

import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostCreateDto {
    private String title;
    private String content;
    private Long categoryId;
    private String videoId;
    private boolean isSecret;
    private boolean isDeleted;
    private boolean voteEnabled;
    private String voteTitle;
    private List<VoteOptionCreateDto> voteOptions;
    private String username;

    public boolean hasVoteOptions() {
        return voteEnabled && voteOptions != null && !voteOptions.isEmpty();
    }

    public boolean isValidVoteSetup() {
        if (!voteEnabled) return true;
        return voteOptions != null && voteOptions.size() >= 2;
    }

}
