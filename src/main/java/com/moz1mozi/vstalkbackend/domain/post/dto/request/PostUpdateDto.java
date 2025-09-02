package com.moz1mozi.vstalkbackend.domain.post.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voteEndTime;

    public boolean hasVoteOptions() {
        return voteOptions != null && !voteOptions.isEmpty();
    }
}
