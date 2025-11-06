package com.nowayback.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.nowayback.user.domain.entity.User;

public interface UserRepository {
	boolean existsByUsernameAndDeletedAtNull(String username);

	boolean existsByUsernameAndDeletedAtIsNull(String masterUsername);

	Optional<User> findByUsernameAndDeletedAtIsNull(String username);
	
	void save(User user);

	Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);
}
