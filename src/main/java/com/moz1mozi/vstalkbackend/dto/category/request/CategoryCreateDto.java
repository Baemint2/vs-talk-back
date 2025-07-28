package com.moz1mozi.vstalkbackend.dto.category.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryCreateDto {

    private String name;
    private String slug;

    @Builder
    public CategoryCreateDto(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
}
