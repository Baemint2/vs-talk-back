package com.moz1mozi.vstalkbackend.entity;

import com.moz1mozi.vstalkbackend.dto.category.response.CategoryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private boolean isDeleted;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;


    public CategoryDto toDto() {
        return CategoryDto.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Builder
    public Category(String name, boolean isDeleted) {
        this.name = name;
        this.isDeleted = isDeleted;
    }
}
