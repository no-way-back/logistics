package com.nowayback.user.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nowayback.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

	Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);

	Optional<User> findByUsernameAndDeletedAtIsNull(String username);

	boolean existsByUsernameAndDeletedAtIsNull(String username);

	Page<User> findAllByDeletedAtIsNull(Pageable pageable);
}