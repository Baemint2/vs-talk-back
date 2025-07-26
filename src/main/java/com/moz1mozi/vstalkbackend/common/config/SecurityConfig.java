package com.moz1mozi.vstalkbackend.common.config;

import com.moz1mozi.vstalkbackend.common.auth.CustomAuthenticationEntryPoint;
import com.moz1mozi.vstalkbackend.common.auth.LoginSuccessHandler;
import com.moz1mozi.vstalkbackend.common.auth.UserSecurityService;
import com.moz1mozi.vstalkbackend.common.auth.oauth2.CustomOAuth2AccessTokenResponseClient;
import com.moz1mozi.vstalkbackend.common.auth.oauth2.OAuth2UserService;
import com.moz1mozi.vstalkbackend.common.filter.JwtAuthorizationFilter;
import com.moz1mozi.vstalkbackend.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final OAuth2UserService oAuth2UserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil, UserSecurityService userSecurityService) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headersConfigurer ->
                    headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/api/v1/bookmark").authenticated()
                            .anyRequest().permitAll())
            .oauth2Login(oauth2 -> oauth2
                            .loginPage("/login")
                    .userInfoEndpoint(userInfo ->
                    userInfo.userService(oAuth2UserService))
                    .tokenEndpoint(token ->
                            token.accessTokenResponseClient(accessTokenResponseClient()))
                    .successHandler(new LoginSuccessHandler(jwtUtil))
            )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, userSecurityService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new CustomOAuth2AccessTokenResponseClient(); // 커스텀 클라이언트 빈 등록
    }
}
