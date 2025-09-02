package com.moz1mozi.vstalkbackend.domain.point.entity;

public enum PointStatus {
    PENDING("대기중"),
    COMPLETED("완료"),
    CANCELLED("취소"),
    EXPIRED("만료");

    private final String description;
    
    PointStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}