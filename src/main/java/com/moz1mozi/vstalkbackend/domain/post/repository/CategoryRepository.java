package com.moz1mozi.vstalkbackend.domain.post.repository;

import com.moz1mozi.vstalkbackend.domain.post.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentIdIsNullOrderByIdAsc();

    Optional<Category> findByName(String name);

    @Query("""
        select c from Category c
        left join fetch c.parent p
        order by 
          case when p.id is null then 0 else 1 end, 
          p.id asc, 
          c.updatedAt asc
    """)
    List<Category> findAllOrderByParentAndName();
}
