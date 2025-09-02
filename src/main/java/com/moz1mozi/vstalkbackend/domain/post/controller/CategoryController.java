package com.moz1mozi.vstalkbackend.domain.post.controller;

import com.moz1mozi.vstalkbackend.common.ApiResponse;
import com.moz1mozi.vstalkbackend.domain.post.dto.request.CategoryCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.CategoryDto;
import com.moz1mozi.vstalkbackend.domain.post.dto.response.CategoryTreeDto;
import com.moz1mozi.vstalkbackend.domain.post.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "카테고리 CRUD")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 조회")
    @GetMapping
    public ApiResponse<List<CategoryDto>> findAllCategoryName(){
        return ApiResponse.ok(categoryService.findAllCategoryName());
    }

    @Operation(summary = "카테고리 tree 조회")
    @GetMapping("/tree")
    public ApiResponse<List<CategoryTreeDto>> findAllCategoryTree(){
        return ApiResponse.ok(categoryService.getCategoryTree());
    }

    @Operation(summary = "카테고리 생성")
    @PostMapping
    public ApiResponse<?> addCategory(@RequestBody CategoryCreateDto dto) {
        return ApiResponse.ok(categoryService.createCategory(dto));
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.of(HttpStatus.NO_CONTENT, null);
    }
}
