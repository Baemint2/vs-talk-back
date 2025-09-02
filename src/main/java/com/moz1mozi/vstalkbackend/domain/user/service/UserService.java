package com.moz1mozi.vstalkbackend.domain.user.service;

import com.moz1mozi.vstalkbackend.domain.user.dto.request.UserCreateDto;
import com.moz1mozi.vstalkbackend.domain.user.entity.ProviderType;
import com.moz1mozi.vstalkbackend.domain.user.entity.Role;
import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import com.moz1mozi.vstalkbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username){
        // 1. provider_key로 먼저 조회
        Optional<User> byProviderKey = userRepository.findByProviderKey(username);
        return byProviderKey.orElseGet(() -> userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다.")));
    }


    public User saveUserIfNotExist(String providerId, String email, String nickname,
                                   String profile, ProviderType providerType) {
        // 1) 존재하면 마지막 로그인 시간만 갱신
        return userRepository.findByProviderKey(providerId)
                .map(user -> {
                    user.changeLastLoginDate();
                    return user;
                })
                .orElseGet(() -> {
                    User user = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .profile(profile)
                            .providerKey(providerId)
                            .providerType(providerType)
                            .role(Role.USER)
                            .build();
                    user.changeLastLoginDate();
                    return userRepository.save(user);
                });
    }

    public User createUser(UserCreateDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .build();
        return userRepository.save(user);
    }
}
