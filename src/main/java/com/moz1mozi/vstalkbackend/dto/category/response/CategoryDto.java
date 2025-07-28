package com.moz1mozi.vstalkbackend.dto.category.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {

    private long id;
    private String name;
    private String slug;

}
