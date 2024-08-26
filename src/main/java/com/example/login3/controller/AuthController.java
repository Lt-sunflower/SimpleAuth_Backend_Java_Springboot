package com.example.login3.controller;

import com.example.login3.model.User;
import com.example.login3.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

		@Autowired
		private AuthService authService;

		@PostMapping("/register")
		public ResponseEntity<User> register(@RequestBody User user) {

				User newUser = authService.register(user);
				if (newUser != null) {
						return new ResponseEntity<>(newUser, HttpStatus.CREATED);
				} else {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
		}

		@PostMapping("/login")
		public ResponseEntity<String> login(HttpServletResponse response, @RequestBody User user) {

				Map<String, String> tokens = authService.login(user);
				if (tokens == null) {
						return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}

				Cookie cookie = new Cookie("refreshToken", tokens.get("refreshToken"));
//        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
//        cookie.setSecure(true);
				cookie.setHttpOnly(true);
				response.addCookie(cookie);

				return new ResponseEntity<>(tokens.get("accessToken"), HttpStatus.OK);
		}

		@PostMapping("/refresh")
		public ResponseEntity<String> refresh(@CookieValue(value = "refreshToken") String refreshToken) {

				String newToken = authService.refresh(refreshToken);
				return new ResponseEntity<>(newToken, HttpStatus.OK);
		}


}
