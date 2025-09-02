package com.moz1mozi.vstalkbackend.domain.point.service;

import com.moz1mozi.vstalkbackend.domain.point.entity.PointStatus;
import com.moz1mozi.vstalkbackend.domain.point.entity.PointTransaction;
import com.moz1mozi.vstalkbackend.domain.point.entity.PointType;
import com.moz1mozi.vstalkbackend.domain.point.entity.UserPoint;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import com.moz1mozi.vstalkbackend.domain.user.entity.UserLevel;
import com.moz1mozi.vstalkbackend.domain.point.repository.PointTransactionRepository;
import com.moz1mozi.vstalkbackend.domain.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PointService {
    
    private final PointTransactionRepository pointTransactionRepository;
    private final UserPointRepository userPointRepository;
    
    public void earnPoints(User user, PointType pointType, Long referenceId) {
        if (pointType.isEarning() && hasAlreadyEarnedPoints(user, pointType, referenceId)) {
            log.warn("이미 포인트를 받은 활동입니다. User: {}, Type: {}, ReferenceId: {}", 
                    user.getId(), pointType, referenceId);
            return;
        }
        
        UserPoint userPoint = getUserPointOrCreate(user);
        
        int baseAmount = pointType.getDefaultAmount();
        int finalAmount = (int) (baseAmount * userPoint.getUserLevel().getPointMultiplier());
        
        userPoint.addPoints(finalAmount);
        
        PointTransaction transaction = PointTransaction.createCompleted(
                user, pointType, finalAmount, userPoint.getCurrentPoints(),
                pointType.getDescription(), referenceId);
        
        pointTransactionRepository.save(transaction);
        
        log.info("포인트 획득 완료 - User: {}, Amount: {}, Balance: {}", 
                user.getId(), finalAmount, userPoint.getCurrentPoints());
    }
    
    public boolean deductPoints(User user, PointType pointType, Long referenceId) {
        UserPoint userPoint = getUserPointOrCreate(user);
        int amount = Math.abs(pointType.getDefaultAmount());
        
        if (userPoint.hasInsufficientPoints(amount)) {
            log.warn("포인트 부족 - User: {}, Required: {}, Current: {}", 
                    user.getId(), amount, userPoint.getCurrentPoints());
            return false;
        }
        
        userPoint.deductPoints(amount);
        
        PointTransaction transaction = PointTransaction.createCompleted(
                user, pointType, -amount, userPoint.getCurrentPoints(),
                pointType.getDescription(), referenceId);
        
        pointTransactionRepository.save(transaction);
        
        log.info("포인트 차감 완료 - User: {}, Amount: {}, Balance: {}", 
                user.getId(), amount, userPoint.getCurrentPoints());
        
        return true;
    }
    
    public boolean checkAdDisplayNeeded(User user) {
        UserPoint userPoint = getUserPointOrCreate(user);
        return userPoint.incrementVoteCount();
    }
    
    public UserPoint getUserPoint(User user) {
        return getUserPointOrCreate(user);
    }
    
    public List<PointTransaction> getPointHistory(User user) {
        return pointTransactionRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    private boolean hasAlreadyEarnedPoints(User user, PointType pointType, Long referenceId) {
        return pointTransactionRepository.existsByUserAndPointTypeAndReferenceIdAndStatus(
                user, pointType, referenceId, PointStatus.COMPLETED);
    }
    
    private UserPoint getUserPointOrCreate(User user) {
        return userPointRepository.findByUser(user)
                .orElseGet(() -> {
                    UserPoint newUserPoint = UserPoint.builder()
                            .user(user)
                            .currentPoints(100)
                            .userLevel(UserLevel.BRONZE)
                            .build();
                    return userPointRepository.save(newUserPoint);
                });
    }
    
    public void processPendingTransactions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        List<PointTransaction> expiredTransactions = 
                pointTransactionRepository.findByStatusAndCreatedAtBefore(
                        PointStatus.PENDING, cutoffTime);
        
        for (PointTransaction transaction : expiredTransactions) {
            transaction.updateStatus(PointStatus.EXPIRED);
            log.info("만료된 포인트 거래 처리: {}", transaction.getId());
        }
    }
}