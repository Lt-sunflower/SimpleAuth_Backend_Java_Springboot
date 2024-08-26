package com.example.login3.service;

import com.example.login3.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface RefreshTokenService {

		void saveToken(Map<String, String> map, User authUser);

		boolean verifyToken(String token);

		@Transactional
		void deleteToken(String token);
}
