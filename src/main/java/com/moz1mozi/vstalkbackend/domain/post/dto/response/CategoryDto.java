package com.moz1mozi.vstalkbackend.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {

    private long id;
    private String name;
    private String slug;
    private Long parentId;

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
