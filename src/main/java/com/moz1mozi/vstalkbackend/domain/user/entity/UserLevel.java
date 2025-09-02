package com.moz1mozi.vstalkbackend.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserLevel {
    BRONZE("브론즈", 1.0, "#CD7F32"),
    SILVER("실버", 1.2, "#C0C0C0"), 
    GOLD("골드", 1.5, "#FFD700"),
    PLATINUM("플래티넘", 2.0, "#E5E4E2");
    
    private final String displayName;
    private final double pointMultiplier;
    private final String colorCode;
    
    UserLevel(String displayName, double pointMultiplier, String colorCode) {
        this.displayName = displayName;
        this.pointMultiplier = pointMultiplier;
        this.colorCode = colorCode;
    }

    public int getRequiredPointsForNext() {
        return switch (this) {
            case BRONZE -> 200;
            case SILVER -> 500;
            case GOLD -> 1000;
            case PLATINUM -> 0;
        };
    }
}