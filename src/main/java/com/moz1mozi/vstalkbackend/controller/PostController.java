package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.ApiResponse;
import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostUpdateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import com.moz1mozi.vstalkbackend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Post", description = "게시글 CRUD 및 조회")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성")
    @PostMapping
    public ApiResponse<Long> createPost(@RequestBody PostCreateDto dto) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name.equals("anonymousUser")) {
            return ApiResponse.unauthorized("로그인이 필요합니다.");
        }
        Long post = postService.createPost(dto, name);
        return ApiResponse.ok(post);
    }

    @Operation(summary = "게시글 조회", description = "정렬 조건에 맞게 조회된다.")
    @GetMapping
    public ApiResponse<List<PostDto>> getPostList(@RequestParam(required = false) String orderBy) {
        return ApiResponse.ok(postService.getPostList(orderBy));
    }

    @Operation(summary = "게시글 조회(카테고리)", description = "카테고리에 맞는 게시글 리스트를 조회한다.")
    @GetMapping("/category/{slug}")
    public ApiResponse<List<PostDto>> getPostListByCategory(@PathVariable String slug) {
        return ApiResponse.ok(postService.getPostListByCategory(slug));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ApiResponse<PostDto> getPost(@PathVariable Long postId) {
        return ApiResponse.ok(postService.getPost(postId));
    }

    @Operation(summary = "게시글 검색")
    @GetMapping("/search")
    public ApiResponse<List<PostDto>> getSearchPostList(@RequestParam(required = false) String orderBy,
                                           @RequestParam String title) {
        return ApiResponse.ok(postService.searchPost(title));
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    public ApiResponse<?> updatePost(@PathVariable Long postId,
                                        @RequestBody PostUpdateDto dto) {
        postService.updatePost(postId, dto);
        return ApiResponse.of(HttpStatus.OK, null);
    }
}
