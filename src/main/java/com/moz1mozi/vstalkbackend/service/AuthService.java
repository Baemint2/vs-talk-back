package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.auth.UserSecurityService;
import com.moz1mozi.vstalkbackend.dto.LoginRequest;
import com.moz1mozi.vstalkbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserSecurityService userSecurityService;
    private final PasswordEncoder passwordEncoder;

    public Authentication login(LoginRequest request) {
        UserDetails userDetails = userSecurityService.loadUserByUsername(request.getUsername());
        if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }


}
