package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
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
    public List<PostDto> getPostList(@RequestParam(required = false) String orderBy) {
        return postService.getPostList(orderBy);
    }

    @GetMapping("/get/category/{slug}")
    public List<PostDto> getPostListByCategory(@PathVariable String slug) {
        return postService.getPostListByCategory(slug);
    }

    @GetMapping("/get/{postId}")
    public PostDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping("/get/search")
    public List<PostDto> getSearchPostList(@RequestParam(required = false) String orderBy,
                                           @RequestParam String title) {
        return postService.searchPost(title);
    }
}
