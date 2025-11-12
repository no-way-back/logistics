package com.nowayback.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nowayback.user.domain.entity.User;

public interface UserRepository {

	User save(User user);

	Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);

	Optional<User> findByUsernameAndDeletedAtIsNull(String username);

	boolean existsByUsernameAndDeletedAtIsNull(String username);

	Page<User> findAllByDeletedAtIsNull(Pageable pageable);
}
