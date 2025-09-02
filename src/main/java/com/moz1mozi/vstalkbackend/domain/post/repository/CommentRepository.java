package com.moz1mozi.vstalkbackend.domain.post.repository;

import com.moz1mozi.vstalkbackend.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);
}
