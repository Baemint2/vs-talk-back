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

    @Query(value = """
    WITH RECURSIVE cat_tree AS (
        SELECT c.category_id, c.parent_id, c.name, c.slug
        FROM category c
        WHERE c.slug = :slug
        UNION ALL
        SELECT c2.category_id, c2.parent_id, c2.name, c2.slug
        FROM category c2
        JOIN cat_tree ct ON c2.parent_id = ct.category_id
    )
    SELECT
        p.*,
        c.category_id,
        c.name,
        c.slug
    FROM post p
    JOIN category c  ON c.category_id = p.category_category_id
    JOIN cat_tree ct ON ct.category_id = c.category_id
    WHERE p.is_deleted = false
    ORDER BY p.created_at desc 
    """,
            nativeQuery = true)
    List<Post> findPostRowsByCategorySlugTree(String slug);

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
