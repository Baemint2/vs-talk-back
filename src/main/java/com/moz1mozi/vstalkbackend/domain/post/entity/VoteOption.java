package com.moz1mozi.vstalkbackend.domain.post.entity;

import com.moz1mozi.vstalkbackend.domain.post.dto.response.VoteOptionDto;
import com.moz1mozi.vstalkbackend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vote_option")
public class VoteOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String optionText;

    private Integer displayOrder;

    private String color;

    @OneToMany(mappedBy = "voteOption", cascade = CascadeType.ALL)
    private List<Vote> votes;

    @Builder
    public VoteOption(Post post, String optionText, Integer displayOrder, String color) {
        this.post = post;
        this.optionText = optionText;
        this.displayOrder = displayOrder;
        this.color = color;
    }

    public void updateOption(String optionText, String color) {
        this.optionText = optionText;
        this.color = color;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public VoteOptionDto toDto() {
        return VoteOptionDto.builder()
                .id(id)
                .optionText(optionText)
                .color(color)
                .build();
    }
}