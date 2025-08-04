package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByIsDeletedFalse(Sort sort);
    List<Post> findByCategorySlugAndIsDeletedFalse(String slug, Sort sort);

    // select * from where title like %검색어%
    List<Post> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title, Sort sort);

}
