package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostUpdateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import com.moz1mozi.vstalkbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostCreateDto dto) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long post = postService.createPost(dto, name);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public List<PostDto> getPostList(@RequestParam(required = false) String orderBy) {
        return postService.getPostList(orderBy);
    }

    @GetMapping("/category/{slug}")
    public List<PostDto> getPostListByCategory(@PathVariable String slug) {
        return postService.getPostListByCategory(slug);
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping("/search")
    public List<PostDto> getSearchPostList(@RequestParam(required = false) String orderBy,
                                           @RequestParam String title) {
        return postService.searchPost(title);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId,
                                        @RequestBody PostUpdateDto dto) {
        postService.updatePost(postId, dto);
        return ResponseEntity.ok().build();
    }
}
