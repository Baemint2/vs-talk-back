package com.moz1mozi.vstalkbackend.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryCreateDto {

    private String name;
    private String slug;
    private Long parentId;

    @Builder
    public CategoryCreateDto(String name, String slug, Long parentId) {
        this.name = name;
        this.slug = slug;
        this.parentId = parentId;
    }
}
