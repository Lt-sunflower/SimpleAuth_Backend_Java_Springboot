package com.example.login3.service;

import com.example.login3.model.User;

import java.util.Map;

public interface AuthService {

		User register(User user);

		Map<String, String> login(User user);

		String refresh(String token);
}
