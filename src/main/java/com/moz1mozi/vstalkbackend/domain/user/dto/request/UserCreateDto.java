package com.moz1mozi.vstalkbackend.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateDto {

    private String username;
    private String nickname;
    private String password;
}
