package com.moz1mozi.vstalkbackend.dto.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateDto {

    private Long id;
    private String title;
    private String content;
    private boolean isSecret;
    private boolean isDeleted;
    private Long categoryId;
    private String videoId;

}
