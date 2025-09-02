package com.moz1mozi.vstalkbackend.domain.post.service;

import com.moz1mozi.vstalkbackend.domain.post.dto.request.CategoryCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.CategoryDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.CategoryTreeDto;
import com.moz1mozi.vstalkbackend.domain.post.entity.Category;
import com.moz1mozi.vstalkbackend.domain.post.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAllCategoryName() {
        return categoryRepository.findAllByParentIdIsNullOrderByIdAsc()
                .stream()
                .map(Category::toDto)
                .toList();
    }

    public List<CategoryTreeDto> getCategoryTree() {
        List<Category> all = categoryRepository.findAllOrderByParentAndName();

        Map<Long, CategoryTreeDto> map = new HashMap<>();
        for (Category c : all) map.put(c.getId(), CategoryTreeDto.of(c));

        List<CategoryTreeDto> roots = new ArrayList<>();
        for (Category c : all) {
            CategoryTreeDto current = map.get(c.getId());
            if (c.getParent() == null) {
                roots.add(current);
            } else {
                map.get(c.getParent().getId()).getChildren().add(current);
            }
        }

        sortRecursively(roots);
        return roots;
    }

    private void sortRecursively(List<CategoryTreeDto> nodes) {
        nodes.forEach(n -> sortRecursively(n.getChildren()));
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }

    public Category createCategory(CategoryCreateDto dto) {

        Category parent = null;
        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리가 존재하지 않습니다."));
        }

        // 동일한 카테고리명이 존재하는지 안하는지 체크하기

        Category category = Category.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .parent(parent)
                .build();

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        categoryRepository.flush();
    }
}
