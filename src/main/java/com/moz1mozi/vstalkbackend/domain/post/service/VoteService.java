package com.moz1mozi.vstalkbackend.domain.post.service;

import com.moz1mozi.vstalkbackend.common.EntityFinder;
import com.moz1mozi.vstalkbackend.domain.post.dto.request.VoteCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.PostVoteCountDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.VoteCountDto;
import com.moz1mozi.vstalkbackend.domain.post.entity.Post;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import com.moz1mozi.vstalkbackend.domain.post.entity.Vote;
import com.moz1mozi.vstalkbackend.domain.post.entity.VoteOption;
import com.moz1mozi.vstalkbackend.domain.post.repository.PostRepository;
import com.moz1mozi.vstalkbackend.domain.post.repository.VoteOptionRepository;
import com.moz1mozi.vstalkbackend.domain.post.repository.VoteRepository;
import com.moz1mozi.vstalkbackend.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final VoteOptionRepository voteOptionRepository;


    // 사용자 투표 처리
    public void vote(VoteCreateDto dto, String username) {
        User user = userService.findByUsername(username);

        Post post = getOrThrow(dto.getPostId());

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
            voteRepository.save(existingVote.get());
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

    // 투표 카운트 조회 ( post ID 기준으로 잡기 )
    public List<VoteCountDto> getVoteCount(Long postId) {
        return voteRepository.countByPostVoteOption(postId);
    }

    // 투표 카운트 조회 ( post ID 기준으로 잡기 )
    public List<PostVoteCountDto> getVotes() {
        return voteRepository.countByPostVoteOption();
    }

    // 특정 유저 투표 여부 확인하기
    public boolean isVoted(Long postId, String username) {
        User user = userService.findByUsername(username);
        Post post = getOrThrow(postId);
        Optional<Vote> existingVote = voteRepository.findByAuthorAndPost(user, post);
        return existingVote.isPresent();
    }

    public Map<Long, Long> getVoteCountsForPosts(List<Long> postIds) {
        // VoteRepository에 메서드 하나만 추가
        return voteRepository.countVotesByPostIds(postIds)
                .stream()
                .collect(Collectors.toMap(
                        result -> result[0],  // postId
                        result -> result[1]   // count
                ));
    }

    private Post getOrThrow(Long postId) {
        return EntityFinder.findOrThrow(postRepository, postId, "존재하지 않는 게시글입니다.");
    }
}
