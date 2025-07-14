package com.moz1mozi.vstalkbackend.auth;


import com.moz1mozi.vstalkbackend.dto.user.UserDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserDto userDto;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(UserDto userDto, List<GrantedAuthority> authorities) {
        this.userDto = userDto;
        this.authorities= authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        if (userDto.getProviderKey() != null && !userDto.getProviderKey().isEmpty()) {
            return userDto.getProviderKey();
        } else {
            return userDto.getUsername();
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
