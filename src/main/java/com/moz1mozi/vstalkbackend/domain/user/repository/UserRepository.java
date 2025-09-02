package com.moz1mozi.vstalkbackend.domain.user.repository;

import com.moz1mozi.vstalkbackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.providerKey = :providerKey AND u.providerKey IS NOT NULL")
    Optional<User> findByProviderKey(@Param("providerKey") String providerKey);

    Optional<User> findByUsername(String username);
}
