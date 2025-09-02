package com.moz1mozi.vstalkbackend.domain.point.repository;

import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import com.moz1mozi.vstalkbackend.domain.point.entity.UserPoint;
import com.moz1mozi.vstalkbackend.domain.user.entity.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
    Optional<UserPoint> findByUser(User user);
    
    List<UserPoint> findTop10ByOrderByCurrentPointsDesc();
    
    List<UserPoint> findByUserLevelIn(List<UserLevel> levels);
    
    @Query("SELECT up FROM UserPoint up WHERE up.currentPoints < :threshold")
    List<UserPoint> findUsersWithLowPoints(int threshold);
}