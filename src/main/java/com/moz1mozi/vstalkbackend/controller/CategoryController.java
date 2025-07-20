package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.category.response.CategoryDto;
import com.moz1mozi.vstalkbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/all")
    public List<CategoryDto> findAllCategoryName(){
        return categoryService.findAllCategoryName();
    }

}
