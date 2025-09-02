package com.moz1mozi.vstalkbackend.domain.post.controller;

import com.moz1mozi.vstalkbackend.domain.post.service.VoteOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/vote/option")
@RequiredArgsConstructor
@Tag(name = "VoteOption", description = "투표옵션")
public class VoteOptionController {

    private final VoteOptionService voteOptionService;

    @Operation(summary = "투표옵션 삭제")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<?> delete(@PathVariable List<Long> optionId) {
        voteOptionService.removeVoteOption(optionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
