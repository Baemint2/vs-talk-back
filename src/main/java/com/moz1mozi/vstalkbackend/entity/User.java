package com.moz1mozi.vstalkbackend.entity;

import com.moz1mozi.vstalkbackend.dto.user.UserDto;
import com.moz1mozi.vstalkbackend.dto.user.UserInfoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    private String username;
    private String password;
    private String email;
    private String profile;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String providerKey;

    private boolean isDeleted;

    @Column(name = "last_login_date")
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    @OneToMany(mappedBy = "author")
    private List<Vote> votes;

    public void changeLastLoginDate() {
        this.lastLogin = LocalDateTime.now();
    }

    public void testUserId(Long id) {
        this.id = id;
    }

    @Builder
    public User(String username, String password, String email, String profile, String nickname, Role role, ProviderType providerType, String providerKey, boolean isDeleted, LocalDateTime lastLogin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
        this.nickname = nickname;
        this.role = role;
        this.providerType = providerType;
        this.providerKey = providerKey;
        this.isDeleted = isDeleted;
        this.lastLogin = lastLogin;
    }

    public UserDto toDto() {
        return UserDto.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .profile(profile)
                .providerKey(providerKey)
                .build();
    }

    public UserInfoDto toInfoDto() {
        return UserInfoDto.builder()
                .id(id)
                .profile(profile)
                .nickname(nickname)
                .build();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", profile='" + profile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", role='" + role + '\'' +
                ", providerType=" + providerType +
                ", providerKey='" + providerKey + '\'' +
                ", isDeleted=" + isDeleted +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
