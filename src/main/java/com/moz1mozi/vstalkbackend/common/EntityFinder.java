package com.moz1mozi.vstalkbackend.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class EntityFinder {
    public static <T, ID> T findOrThrow(JpaRepository<T, ID> repository, ID id, String message) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(message));
    }
}
