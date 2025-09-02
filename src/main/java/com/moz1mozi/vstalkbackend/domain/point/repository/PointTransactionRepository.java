package com.moz1mozi.vstalkbackend.domain.point.repository;

import com.moz1mozi.vstalkbackend.domain.point.entity.PointTransaction;
import com.moz1mozi.vstalkbackend.domain.point.entity.PointType;
import com.moz1mozi.vstalkbackend.domain.point.entity.PointStatus;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    boolean existsByUserAndPointTypeAndReferenceIdAndStatus(
            User user, PointType pointType, Long referenceId, PointStatus status);
    
    List<PointTransaction> findByUserOrderByCreatedAtDesc(User user);
    
    List<PointTransaction> findByUserAndPointTypeInOrderByCreatedAtDesc(User user, List<PointType> pointTypes);
    
    List<PointTransaction> findByReferenceIdAndPointType(Long referenceId, PointType pointType);
    
    List<PointTransaction> findByStatusAndCreatedAtBefore(PointStatus status, LocalDateTime cutoffTime);
    
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PointTransaction pt " +
           "WHERE pt.user = :user AND pt.status = :status " +
           "AND pt.createdAt BETWEEN :startDate AND :endDate")
    int sumPointsByUserAndPeriod(@Param("user") User user, 
                               @Param("status") PointStatus status,
                               @Param("startDate") LocalDateTime startDate, 
                               @Param("endDate") LocalDateTime endDate);
}