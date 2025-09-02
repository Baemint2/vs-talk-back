package com.moz1mozi.vstalkbackend.domain.post.entity;

import com.moz1mozi.vstalkbackend.common.entity.BaseTimeEntity;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class Vote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_option_id")
    private VoteOption voteOption; // 선택한 옵션

    @Builder
    public Vote(User author, Post post, VoteOption voteOption) {
        this.author = author;
        this.post = post;
        this.voteOption = voteOption;
    }

    // 투표 변경 메서드
    public void changeVoteOption(VoteOption newVoteOption) {
        this.voteOption = newVoteOption;
    }

}
