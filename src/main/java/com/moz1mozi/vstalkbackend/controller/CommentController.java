package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.UpdateCommentDto;
import com.moz1mozi.vstalkbackend.dto.comment.request.CommentCreateDto;
import com.moz1mozi.vstalkbackend.dto.comment.response.CommentDto;
import com.moz1mozi.vstalkbackend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comment", description = "댓글 CRUD 및 조회")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 조회")
    @GetMapping("/{postId}")
    public List<CommentDto> getCommentList(@PathVariable Long postId) {
        return commentService.getComment(postId);
    }

    @Operation(summary = "댓글 추가")
    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody CommentCreateDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.saveComment(dto, username));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.removeComment(commentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody UpdateCommentDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentDto commentDto = commentService.updateCount(commentId, dto.getContent(), username);
        return ResponseEntity.ok(commentDto);
    }
}
