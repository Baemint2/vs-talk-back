package com.moz1mozi.vstalkbackend.dto.post;

import com.moz1mozi.vstalkbackend.dto.VoteOptionDto;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private String videoId;
    private List<VoteOptionDto> voteOptionList;
    private boolean isSecret;
    private boolean isDeleted;
    private boolean voteEnabled;
    private LocalDateTime updatedAt;
}
