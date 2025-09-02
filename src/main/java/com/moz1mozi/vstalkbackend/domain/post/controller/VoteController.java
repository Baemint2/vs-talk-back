package com.moz1mozi.vstalkbackend.domain.post.controller;

import com.moz1mozi.vstalkbackend.domain.post.dto.request.VoteCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.service.VoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "Vote", description = "투표 CRUD")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<?> selectVote(@RequestBody VoteCreateDto voteCreateDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        voteService.vote(voteCreateDto, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<?> getVoteCount(@PathVariable Long postId) {
        log.info("postId: {}", postId);
        return ResponseEntity.ok(voteService.getVoteCount(postId));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getVoteCount() {
        return ResponseEntity.ok(voteService.getVotes());
    }


    @GetMapping("/{postId}/status")
    public ResponseEntity<?> isVoted(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("postId: {}, username: {}", postId, username);
        return ResponseEntity.ok(voteService.isVoted(postId, username));
    }
}
