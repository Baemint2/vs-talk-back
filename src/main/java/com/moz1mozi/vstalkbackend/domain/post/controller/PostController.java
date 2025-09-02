package com.moz1mozi.vstalkbackend.domain.post.controller;

import com.moz1mozi.vstalkbackend.common.ApiResponse;
import com.moz1mozi.vstalkbackend.common.dto.SliceResponse;
import com.moz1mozi.vstalkbackend.domain.post.dto.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.request.PostUpdateDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.PostDto;
import com.moz1mozi.vstalkbackend.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public SliceResponse<PostDto> list(
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Slice<PostDto> slice = postService.getPostList(orderBy, page, size);
        return new SliceResponse<>(slice.getContent(), slice.hasNext(), slice.getNumber(), slice.getSize());
    }


    @Operation(summary = "게시글 조회(카테고리)", description = "카테고리에 맞는 게시글 리스트를 조회한다.")
    @GetMapping("/category/{slug}")
    public SliceResponse<PostDto> getPostListByCategory(@PathVariable String slug,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                        @RequestParam(required = false) String orderBy) {
        Slice<PostDto> slice = postService.getPostListByCategory(slug, page, size, orderBy);
        return new SliceResponse<>(slice.getContent(), slice.hasNext(), slice.getNumber(), slice.getSize());
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ApiResponse<PostDto> getPost(@PathVariable Long postId) {
        return ApiResponse.ok(postService.getPost(postId));
    }

    @Operation(summary = "게시글 검색")
    @GetMapping("/search")
    public SliceResponse<PostDto> getSearchPostList(@RequestParam(required = false) String orderBy,
                                                        @RequestParam String title,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        Slice<PostDto> slice = postService.searchPost(title, page, size);
        return new SliceResponse<>(slice.getContent(), slice.hasNext(), slice.getNumber(), slice.getSize());
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

    // 투표 연장
    @Operation(summary = "투표 연장")
    @PutMapping("/{postId}/extend-vote")
    public ApiResponse<?> extendVote(@PathVariable Long postId) {
        postService.updatePostVoteEndTime(postId);
        return ApiResponse.of(HttpStatus.OK, null);
    }
}
