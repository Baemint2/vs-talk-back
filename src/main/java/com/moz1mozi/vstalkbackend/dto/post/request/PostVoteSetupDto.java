package com.moz1mozi.vstalkbackend.dto.post.request;

import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostVoteSetupDto {
    private List<VoteOptionCreateDto> options;
}
