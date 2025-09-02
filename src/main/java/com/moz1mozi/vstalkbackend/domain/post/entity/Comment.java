package com.moz1mozi.vstalkbackend.domain.post.entity;

import com.moz1mozi.vstalkbackend.domain.post.dto.request.CommentCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.CommentDto;
import com.moz1mozi.vstalkbackend.common.entity.BaseTimeEntity;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String content;

    @Column(columnDefinition = "bit DEFAULT false")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, User author, Post post, Comment parent, List<Comment> children) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.parent = parent;
    }

    public static Comment createComment(CommentCreateDto dto, User user,
                                     Post post, Comment childComment) {
        return Comment.builder()
                .content(dto.getContent())
                .author(user)
                .post(post)
                .parent(dto.getParentId() != null ?
                        childComment : null)
                .build();
    }

    public void updateContent(String newContent) {
        if (isDeleted) {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
        }
        this.content = newContent;
    }

    public void markAsDeleted() {
        this.content = "삭제된 댓글입니다.";
        this.isDeleted = true;
    }

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(id)
                .content(content)
                .postId(post.getId())
                .nickname(author.getNickname())
                .username(author.getUsername() != null ? author.getUsername() : author.getProviderKey())
                .parentId(parent != null ? parent.getId() : null)
                .updatedAt(getUpdatedAt())
                .isDeleted(isDeleted)
                .build();
    }
}
