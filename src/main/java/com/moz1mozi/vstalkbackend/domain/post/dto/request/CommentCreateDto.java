package com.moz1mozi.vstalkbackend.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateDto {
    private String content;
    private Long postId;
    private Long parentId;

    @Builder
    public CommentCreateDto(String content, Long postId, Long parentId) {
        this.content = content;
        this.postId = postId;
        this.parentId = parentId;
    }
}
