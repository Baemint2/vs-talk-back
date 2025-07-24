package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.post.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.PostVoteSetupDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteCreateDto;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.entity.Vote;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import com.moz1mozi.vstalkbackend.repository.VoteOptionRepository;
import com.moz1mozi.vstalkbackend.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostService postService;
    private final UserService userService;
    private final VoteOptionRepository voteOptionRepository;


    // 사용자 투표 처리
    public void vote(VoteCreateDto dto) {
        User user = userService.findByUsername(dto.getUsername());

        Post post = postService.getPostId(dto.getPostId());
        VoteOption voteOption = voteOptionRepository.findById(dto.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 투표 옵션입니다."));

        // 투표 옵션이 해당 게시물의 것인지 검증
        if (!voteOption.getPost().getId().equals(dto.getPostId())) {
            throw new IllegalArgumentException("해당 게시물의 투표 옵션이 아닙니다.");
        }

        // 기존 투표가 있는지 확인
        Optional<Vote> existingVote = voteRepository.findByAuthorAndPost(user, post);

        if (existingVote.isPresent()) {
            // 기존 투표 변경
            existingVote.get().changeVoteOption(voteOption);
            log.info("투표 변경: 사용자 {}, 게시물 {}, 옵션 {}",
                    user.getUsername(), post.getId(), voteOption.getId());
        } else {
            // 새로운 투표 생성
            Vote newVote = Vote.builder()
                    .author(user)
                    .post(post)
                    .voteOption(voteOption)
                    .build();

            voteRepository.save(newVote);
            log.info("새 투표 생성: 사용자 {}, 게시물 {}, 옵션 {}",
                    user.getUsername(), post.getId(), voteOption.getId());
        }
    }

}
