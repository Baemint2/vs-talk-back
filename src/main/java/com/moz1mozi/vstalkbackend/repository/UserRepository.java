package com.moz1mozi.vstalkbackend.repository;

import com.moz1mozi.vstalkbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderKey(String providerKey);

    Optional<User> findByUsername(String username);
}
