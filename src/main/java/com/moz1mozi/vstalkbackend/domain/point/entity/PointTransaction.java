package com.moz1mozi.vstalkbackend.domain.point.entity;

import com.moz1mozi.vstalkbackend.common.entity.BaseTimeEntity;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    private int amount;
    private int balanceAfter;

    private String description;
    private Long referenceId;

    @Enumerated(EnumType.STRING)
    private PointStatus status;

    private LocalDateTime completedAt;

    @Builder
    public PointTransaction(User user, PointType pointType, int amount,
                            int balanceAfter, String description, Long referenceId,
                            PointStatus status) {
        this.user = user;
        this.pointType = pointType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.referenceId = referenceId;
        this.status = status != null ? status : PointStatus.COMPLETED;

        if (this.status == PointStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public void updateStatus(PointStatus newStatus) {
        this.status = newStatus;
        if (newStatus == PointStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public static PointTransaction createCompleted(User user, PointType pointType,
                                                   int finalAmount, int newBalance,
                                                   String description, Long referenceId) {
        return PointTransaction.builder()
                .user(user)
                .pointType(pointType)
                .amount(finalAmount)
                .balanceAfter(newBalance)
                .description(description)
                .referenceId(referenceId)
                .status(PointStatus.COMPLETED)
                .build();
    }

    public static PointTransaction createPending(User user, PointType pointType,
                                                 String description, Long referenceId) {
        return PointTransaction.builder()
                .user(user)
                .pointType(pointType)
                .amount(0)
                .balanceAfter(0)
                .description(description)
                .referenceId(referenceId)
                .status(PointStatus.PENDING)
                .build();
    }
}