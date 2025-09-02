package com.moz1mozi.vstalkbackend.domain.point.entity;

import lombok.Getter;

@Getter
public enum PointType {
    QUIZ_CORRECT("퀴즈 정답", 3),
    FRIEND_REFERRAL("친구 추천", 50),
    AD_WATCH("광고 시청", 5),
    AD_WATCH_BONUS("보너스 광고 시청", 20),
    DAILY_LOGIN("출석 체크", 2),
    STREAK_BONUS("연속 출석 보너스", 10),
    WELCOME_BONUS("신규 가입 보너스", 100),
    
    VOTE_PARTICIPATE("투표 참여", -1),
    QUIZ_WRONG("퀴즈 오답", -1);
    
    private final String description;
    private final int defaultAmount;
    
    PointType(String description, int defaultAmount) {
        this.description = description;
        this.defaultAmount = defaultAmount;
    }
    
}