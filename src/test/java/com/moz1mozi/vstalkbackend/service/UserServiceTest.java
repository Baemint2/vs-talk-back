package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.user.request.UserCreateDto;
import com.moz1mozi.vstalkbackend.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("유저를 추가한다.")
    void createUser() {
        // given
        UserCreateDto user = UserCreateDto.builder()
                .username("moz1mozi")
                .password("1234")
                .nickname("모지모지")
                .build();
        // when
        User createUser = userService.createUser(user);

        // then
        assertEquals(createUser.getUsername(), user.getUsername());
        assertEquals(createUser.getNickname(), user.getNickname());
    }
}