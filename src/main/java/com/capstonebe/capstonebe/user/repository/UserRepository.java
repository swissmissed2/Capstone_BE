package com.capstonebe.capstonebe.user.repository;

import com.capstonebe.capstonebe.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findByNameContainingOrEmailContaining(String nameKeyword, String emailKeyword, Pageable pageable);
}
