package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.post.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.PostDto;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@RequestBody PostCreateDto dto) {
        Long post = postService.createPost(dto);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/get")
    public List<PostDto> getPostList() {
        return postService.getPostList();
    }

    @GetMapping("/get/{postId}")
    public PostDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }
}
