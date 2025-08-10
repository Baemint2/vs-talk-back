package com.moz1mozi.vstalkbackend.dto.category.response;

import com.moz1mozi.vstalkbackend.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CategoryTreeDto {

    private long id;
    private String name;
    private String slug;
    private Long parentId;
    List<CategoryTreeDto> children;


    public static CategoryTreeDto of(Category category) {
        return new CategoryTreeDto(category.getId(), category.getName(), category.getSlug(),
                category.getParent() == null ? null : category.getParent().getId(),
                new ArrayList<>());
    }
}
