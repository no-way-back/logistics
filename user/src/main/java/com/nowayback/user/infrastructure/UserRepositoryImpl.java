package com.nowayback.user.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId) {
		return userJpaRepository.findByUserIdAndDeletedAtIsNull(userId);
	}

	@Override
	public Optional<User> findByUsernameAndDeletedAtIsNull(String username) {
		return userJpaRepository.findByUsernameAndDeletedAtIsNull(username);
	}

	@Override
	public boolean existsByUsernameAndDeletedAtIsNull(String username) {
		return userJpaRepository.existsByUsernameAndDeletedAtIsNull(username);
	}
}