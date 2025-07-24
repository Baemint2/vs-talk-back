package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
