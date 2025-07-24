package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.vote.request.VoteCreateDto;
import com.moz1mozi.vstalkbackend.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/add")
    public ResponseEntity<?> selectVote(@RequestBody VoteCreateDto voteCreateDto) {
        voteService.vote(voteCreateDto);
        return ResponseEntity.ok().build();
    }
}
