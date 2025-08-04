package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.dto.auth.LoginRequest;
import com.moz1mozi.vstalkbackend.dto.user.response.UserDto;
import com.moz1mozi.vstalkbackend.service.AuthService;
import com.moz1mozi.vstalkbackend.service.UserService;
import com.moz1mozi.vstalkbackend.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.moz1mozi.vstalkbackend.common.utils.CookieUtil.deleteCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/v1/userInfo")
    public ResponseEntity<?> getUserInfo(Principal principal) {

        UserDto byUsername = userService.findByUsername(principal.getName()).toDto();
        return ResponseEntity.ok(byUsername);
    }

    @GetMapping("/api/v1/loginCheck")
    public Boolean loginCheck() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(name == null) {
            return false;
        }

        UserDto user = userService.findByUsername(name).toDto();
        log.info("loginCheck: {}", user != null);
        return user != null;
    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");

        return ResponseEntity.ok("로그아웃 성공");

    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication auth = authService.login(request);

        String accessToken = jwtUtil.createAccessToken(auth);
        String refreshToken = jwtUtil.createRefreshToken(auth);
        jwtUtil.setTokensAndCookies(accessToken, refreshToken, response);
        log.info("authentication: {}", auth.getName());
        return ResponseEntity.ok(auth.getName());
    }


}
