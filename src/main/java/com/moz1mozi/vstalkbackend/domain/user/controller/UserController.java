package com.moz1mozi.vstalkbackend.domain.user.controller;

import com.moz1mozi.vstalkbackend.common.ApiResponse;
import com.moz1mozi.vstalkbackend.domain.user.dto.LoginRequest;
import com.moz1mozi.vstalkbackend.domain.user.dto.response.UserDto;
import com.moz1mozi.vstalkbackend.domain.user.service.AuthService;
import com.moz1mozi.vstalkbackend.domain.user.service.UserService;
import com.moz1mozi.vstalkbackend.common.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.moz1mozi.vstalkbackend.common.utils.CookieUtil.deleteCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "user", description = "유저 로그인 및 정보 조회")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "유저 정보 조회")
    @GetMapping("/userInfo")
    public ApiResponse<?> getUserInfo(Principal principal) {

        UserDto byUsername = userService.findByUsername(principal.getName()).toDto();
        return ApiResponse.ok(byUsername);
    }

    @Operation(summary = "로그인 여부 체크")
    @GetMapping("/loginCheck")
    public ApiResponse<Boolean> loginCheck() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(name.equals("anonymousUser")) {
            return ApiResponse.ok(false);
        }

        UserDto user = userService.findByUsername(name).toDto();
        return ApiResponse.ok(user != null);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");

        return ResponseEntity.ok("로그아웃 성공");

    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication auth = authService.login(request);

        String accessToken = jwtUtil.createAccessToken(auth);
        String refreshToken = jwtUtil.createRefreshToken(auth);
        jwtUtil.setTokensAndCookies(accessToken, refreshToken, response);
        log.info("authentication: {}", auth.getName());
        return ResponseEntity.ok(auth.getName());
    }


}
