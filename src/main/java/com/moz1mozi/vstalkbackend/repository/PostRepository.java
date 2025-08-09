package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByIsDeletedFalse(Sort sort);
    List<Post> findByCategorySlugAndIsDeletedFalse(String slug, Sort sort);

    // select * from where title like %검색어%
    List<Post> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title, Sort sort);

    @Modifying
    @Query("UPDATE Post p " +
            "SET p.voteEnabled = false " +
            "WHERE p.voteEndTime < NOW() AND p.voteEnabled = true")
    void disableVotesPastDeadline(LocalDateTime now);

    @Query("SELECT p " +
            "FROM Post p " +
            "left join Vote v on v.post = p " +
            "WHERE p.isDeleted = false " +
            "group by p " +
            "order by count(v) desc, p.createdAt desc")
    List<Post> findTopVotedPosts();

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.voteEnabled = true
      AND p.voteEndTime > :now
      AND p.isDeleted = false
    ORDER BY p.voteEndTime ASC
    """)
    List<Post> findVoteActivePosts(@Param("now") LocalDateTime now);

}
