package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByIdAsc();
}
