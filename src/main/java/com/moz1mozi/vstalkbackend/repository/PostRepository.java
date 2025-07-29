package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByOrderByCreatedAtAsc();

    List<Post> findByCategorySlugOrderByCreatedAtDesc(String slug);

    // select * from where title like %검색어%
    List<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
}
