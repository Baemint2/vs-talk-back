package com.moz1mozi.vstalkbackend.domain.post.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voteEndTime;
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
