package com.moz1mozi.vstalkbackend.domain.post.repository;

import com.moz1mozi.vstalkbackend.domain.post.entity.Post;
import com.moz1mozi.vstalkbackend.domain.post.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {

    List<VoteOption> findAllByPostId(Long postId);

    int countByPost(Post post);
}
