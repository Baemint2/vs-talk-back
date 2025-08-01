package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.category.request.CategoryCreateDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CategoryServiceTest {


    @Autowired
    CategoryService categoryService;

    @DisplayName("카테고리를 추가한다.")
    @Test
    void createCategory() {
        // given
        CategoryCreateDto category = CategoryCreateDto.builder()
                .name("스포츠")
                .slug("sports")
                .build();
        // when
        Category savedCategory = categoryService.createCategory(category);
        // thenR
        assertThat(savedCategory.getName()).isEqualTo("스포츠");
    }

    @DisplayName("특정 카테고리를 삭제한다.")
    @Test
    void test() {
        // given
        CategoryCreateDto category = CategoryCreateDto.builder()
                .name("스포츠")
                .slug("sports")
                .build();
        Category savedCategory = categoryService.createCategory(category);
        // when
        categoryService.deleteCategory(savedCategory.getId());
        // then
        assertThatThrownBy( () -> categoryService.getCategory(savedCategory.getId()))
                .hasMessage("카테고리를 찾을 수 없습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

}