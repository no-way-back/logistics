package com.nowayback.user.infrastructure.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.nowayback.common.security.annotation.UserRole;

@Slf4j
@Component
@RequiredArgsConstructor
public class MasterAccountInitializer implements ApplicationRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) {
		String masterUsername = "master";

		if (!userRepository.existsByUsernameAndDeletedAtIsNull(masterUsername)) {
			String encodedPassword = passwordEncoder.encode("Master123!");

			User master = User.createUser(
				masterUsername,
				encodedPassword,
				UserRole.MASTER,
				null
			);

			master.approveSignup();

			userRepository.save(master);
			log.info("마스터 계정이 초기화되었습니다. username: {}", masterUsername);
		}
	}
}
