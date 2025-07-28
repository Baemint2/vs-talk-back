package com.moz1mozi.vstalkbackend.entity;

import com.moz1mozi.vstalkbackend.dto.category.response.CategoryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private String slug;

    @Column(columnDefinition = "bit DEFAULT false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 양방향 관계 추가 - 하위 카테고리들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    public CategoryDto toDto() {
        return CategoryDto.builder()
                .id(id)
                .name(name)
                .slug(slug)
                .build();
    }

    @Builder
    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public boolean isMainCategory() {
        return parent == null;
    }

    public boolean isSubCategory() {
        return parent != null;
    }

    public void addChild(Category child) {
        children.add(child);
        child.parent = this;
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.parent = null;
    }

}
