package com.nowayback.user.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nowayback.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

	boolean existsByUsernameAndDeletedAtIsNull(String username);

	Optional<User> findByUsernameAndDeletedAtIsNull(String username);

	Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);
}