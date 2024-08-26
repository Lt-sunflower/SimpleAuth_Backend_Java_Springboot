package com.example.login3.service.impl;

import com.example.login3.model.User;
import com.example.login3.repository.UserRepository;
import com.example.login3.service.AuthService;
import com.example.login3.service.JwtService;
import com.example.login3.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

		@Autowired
		private UserRepository userRepository;
		@Autowired
		private RefreshTokenService refreshTokenService;
		@Autowired
		private PasswordEncoder passwordEncoder;
		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		private JwtService jwtService;


		@Override
		public User register(User user) {

				if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
						log.info("User {} already exist", user.getUsername());
						return null;
				}
				User newUser = new User();
				newUser.setUsername(user.getUsername());
				newUser.setPassword(passwordEncoder.encode(user.getPassword()));
				newUser.setRole(user.getRole());
				userRepository.save(newUser);

				return newUser;
		}

		@Override
		public Map<String, String> login(User user) {

				try {
						// case-insensitive
						Authentication authentication = authenticationManager.authenticate(
								new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
						);
						log.info("User found, {}", authentication.getPrincipal());

						Map<String, String> map = jwtService.generateNewTokenMap(user);
						refreshTokenService.saveToken(map, (User) authentication.getPrincipal());
						return map;
				} catch (AuthenticationException e) {
						log.info("Bad credentials, {}", user);
						return null;
				}
		}

		@Override
		public String refresh(String token) {

				if (refreshTokenService.verifyToken(token)) {
						return jwtService.generateNewAccessToken(token);
				}
				return null;
		}
}
