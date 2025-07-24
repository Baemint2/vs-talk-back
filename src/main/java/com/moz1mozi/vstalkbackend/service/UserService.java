package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.auth.UserSecurityService;
import com.moz1mozi.vstalkbackend.dto.LoginRequest;
import com.moz1mozi.vstalkbackend.dto.user.UserCreateDto;
import com.moz1mozi.vstalkbackend.entity.ProviderType;
import com.moz1mozi.vstalkbackend.entity.Role;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username){
        log.info("username: {}", username);
        return userRepository.findByProviderKey(username)
                .or(() -> userRepository.findByUsername(username))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public User saveUserIfNotExist(String providerId, String email, String nickname, String profile, ProviderType providerType) {
        log.info("[saveUserIfNotExist] providerId: {}", providerId);
        User existUser = userRepository.findByProviderKey(providerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        System.out.println("[saveUserIfNotExist] = " + existUser);
        if(existUser == null) {
            User user = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .profile(profile)
                    .providerKey(providerId)
                    .providerType(providerType)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        existUser.changeLastLoginDate();
        return existUser;
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
