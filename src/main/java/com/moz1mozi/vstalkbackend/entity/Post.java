package com.moz1mozi.vstalkbackend.entity;

import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    private String videoId;

    private boolean isDeleted;

    // 비밀글
    private boolean isSecret;

    private long viewCount;
    private long commentCount;

    // 투표 활성화 여부
    private boolean voteEnabled = false;

    // 투표 종료 시간 (선택적)
    private LocalDateTime voteEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    @Builder
    public Post(Long id, String title, String content, String videoId, boolean isDeleted, boolean isSecret, long viewCount, long commentCount, boolean voteEnabled, LocalDateTime voteEndTime, User author, Category category, List<Comment> comments, List<Vote> votes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.videoId = videoId;
        this.isDeleted = isDeleted;
        this.isSecret = isSecret;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.voteEnabled = voteEnabled;
        this.voteEndTime = voteEndTime;
        this.author = author;
        this.category = category;
        this.comments = comments;
        this.votes = votes;
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        if (this.commentCount > 0) this.commentCount--;
    }

    public void changeDeleted() {
        this.title = "삭제된 게시글입니다.";
        this.isDeleted = true;
    }

    public void updatePost(String title, String content, String videoId, boolean isDeleted, boolean isSecret, Category category, LocalDateTime voteEndTime) {
        this.title = title;
        this.content = content;
        this.videoId = videoId;
        this.isDeleted = isDeleted;
        this.isSecret = isSecret;
        this.voteEndTime = voteEndTime;
        this.category = category;
    }

    public PostDto toDto() {
        return PostDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .categoryId(category.getId())
                .categoryName(category.getName())
                .commentCount(commentCount)
                .videoId(videoId)
                .author(author.getUsername())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .voteEnabled(voteEnabled)
                .voteEndTime(voteEndTime)
                .isDeleted(isDeleted)
                .build();
    }
}
