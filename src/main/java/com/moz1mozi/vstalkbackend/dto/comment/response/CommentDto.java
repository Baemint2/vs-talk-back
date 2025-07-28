package com.moz1mozi.vstalkbackend.dto.comment.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private Long id;
    private String content;
    private Long postId;
    private String username;
    private Long parentId;
    private LocalDateTime updatedAt;

    @Builder
    public CommentDto(Long id, String content, Long postId, String username, Long parentId, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.username = username;
        this.parentId = parentId;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", postId=" + postId +
                ", username=" + username +
                ", parentId=" + parentId +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
