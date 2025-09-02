package com.moz1mozi.vstalkbackend.domain.point.entity;

import com.moz1mozi.vstalkbackend.common.entity.BaseTimeEntity;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import com.moz1mozi.vstalkbackend.domain.user.entity.UserLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends BaseTimeEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_point_id")
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    private int currentPoints;
    private int totalEarned;
    private int totalSpent;
    
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;
    
    private LocalDateTime lastEarnedAt;
    private int dailyVoteCount;
    private LocalDateTime lastVoteDate;
    
    @Builder
    public UserPoint(User user, int currentPoints, UserLevel userLevel) {
        this.user = user;
        this.currentPoints = currentPoints;
        this.totalEarned = currentPoints;
        this.totalSpent = 0;
        this.userLevel = userLevel != null ? userLevel : UserLevel.BRONZE;
        this.lastEarnedAt = LocalDateTime.now();
        this.dailyVoteCount = 0;
        this.lastVoteDate = LocalDateTime.now().minusDays(1);
    }
    
    public void addPoints(int amount) {
        if (amount <= 0) return;
        
        this.currentPoints += amount;
        this.totalEarned += amount;
        this.lastEarnedAt = LocalDateTime.now();
        updateUserLevel();
    }
    
    public void deductPoints(int amount) {
        if (amount <= 0) return;
        if (this.currentPoints < amount) {
            throw new IllegalArgumentException("보유 포인트가 부족합니다. 현재: " + currentPoints + ", 필요: " + amount);
        }
        
        this.currentPoints -= amount;
        this.totalSpent += amount;
    }
    
    public boolean incrementVoteCount() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        
        if (lastVoteDate.isBefore(today)) {
            this.dailyVoteCount = 0;
            this.lastVoteDate = LocalDateTime.now();
        }
        
        this.dailyVoteCount++;
        return this.dailyVoteCount % 5 == 0;
    }
    
    private void updateUserLevel() {
        if (totalEarned >= 1000) {
            this.userLevel = UserLevel.PLATINUM;
        } else if (totalEarned >= 500) {
            this.userLevel = UserLevel.GOLD;
        } else if (totalEarned >= 200) {
            this.userLevel = UserLevel.SILVER;
        }
    }
    
    public boolean hasInsufficientPoints(int requiredAmount) {
        return this.currentPoints < requiredAmount;
    }
}