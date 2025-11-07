package com.nowayback.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.nowayback.user.domain.entity.User;

public interface UserRepository {

	User save(User user);

	Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);

	Optional<User> findByUsernameAndDeletedAtIsNull(String username);

	boolean existsByUsernameAndDeletedAtIsNull(String username);
}
