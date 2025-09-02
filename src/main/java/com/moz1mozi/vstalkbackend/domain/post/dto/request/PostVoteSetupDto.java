package com.moz1mozi.vstalkbackend.domain.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostVoteSetupDto {
    private List<VoteOptionCreateDto> options;
}
