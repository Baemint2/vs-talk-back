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
    private String providerKey;
    private String nickname;
    private Long parentId;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    @Builder
    public CommentDto(Long id, String content, Long postId, String nickname, String username, String providerKey, Long parentId, LocalDateTime updatedAt, boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.nickname = nickname;
        this.username = username;
        this.providerKey = providerKey;
        this.parentId = parentId;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", postId=" + postId +
                ", nickname=" + nickname +
                ", parentId=" + parentId +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
