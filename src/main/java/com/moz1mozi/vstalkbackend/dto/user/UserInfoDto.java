package com.moz1mozi.vstalkbackend.dto.user;

import com.moz1mozi.vstalkbackend.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {

    private long id;
    private String profile;
    private String nickname;

    public User toEntity() {
        return User.builder()
                .profile(profile)
                .nickname(nickname)
                .build();
    }
}
