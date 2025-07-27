package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.category.request.CategoryCreateDto;
import com.moz1mozi.vstalkbackend.dto.category.response.CategoryDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAllCategoryName(){
        return categoryRepository.findAllByOrderByIdAsc()
                .stream()
                .map(Category::toDto)
                .toList();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }
}
