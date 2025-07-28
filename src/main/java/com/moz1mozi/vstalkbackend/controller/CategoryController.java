package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.category.request.CategoryCreateDto;
import com.moz1mozi.vstalkbackend.dto.category.response.CategoryDto;
import com.moz1mozi.vstalkbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryDto> findAllCategoryName(){
        return categoryService.findAllCategoryName();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryCreateDto dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
