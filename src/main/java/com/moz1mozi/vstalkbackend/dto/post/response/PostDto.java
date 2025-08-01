package com.moz1mozi.vstalkbackend.dto.post.response;

import com.moz1mozi.vstalkbackend.dto.vote.response.VoteOptionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private String videoId;
    private String author;
    private List<VoteOptionDto> voteOptionList;
    private long commentCount;
    private boolean isSecret;
    private boolean isDeleted;
    private boolean voteEnabled;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", videoId='" + videoId + '\'' +
                ", voteOptionList=" + voteOptionList +
                ", isSecret=" + isSecret +
                ", isDeleted=" + isDeleted +
                ", voteEnabled=" + voteEnabled +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
