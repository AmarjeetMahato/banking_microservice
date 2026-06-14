package com.auth.domain.Users.repository;

import com.auth.domain.Users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

    // Find by phone
    Optional<User> findByPhone(String phone);

    // Check if phone already exists (Useful for your validation/service layer)
    boolean existsByPhone(String phone);

    // Check if email already exists
    boolean existsByEmail(String email);
}
