package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.vote.request.VoteCreateDto;
import com.moz1mozi.vstalkbackend.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/add")
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

    @GetMapping("/{postId}/status")
    public ResponseEntity<?> isVoted(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("postId: {}, username: {}", postId, username);
        return ResponseEntity.ok(voteService.isVoted(postId, username));
    }
}
