package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.dto.vote.response.PostVoteCountDto;
import com.moz1mozi.vstalkbackend.dto.vote.response.VoteCountDto;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByAuthorAndPost(User user, Post post);

    @Query("SELECT new com.moz1mozi.vstalkbackend.dto.vote.response.VoteCountDto(v.voteOption.id, COUNT(v.id)) " +
            "FROM Vote v WHERE v.post.id = :postId GROUP BY v.voteOption.id ORDER BY v.voteOption.id")
    List<VoteCountDto> countByPostVoteOption(@Param("postId") Long postId);

    @Query("SELECT new com.moz1mozi.vstalkbackend.dto.vote.response.PostVoteCountDto(v.post.id, COUNT(v.id)) " +
            "FROM Vote v group by v.post.id ORDER BY v.post.id")
    List<PostVoteCountDto> countByPostVoteOption();

    @Query("""
        SELECT v.post.id, COUNT(v)
        FROM Vote v
        WHERE v.post.id IN :postIds
        GROUP BY v.post.id
        """)
    List<Long[]> countVotesByPostIds(@Param("postIds") List<Long> postIds);
}
