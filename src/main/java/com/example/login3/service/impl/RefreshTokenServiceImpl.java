package com.example.login3.service.impl;

import com.example.login3.model.User;
import com.example.login3.model.token.RefreshToken;
import com.example.login3.repository.RefreshTokenRepository;
import com.example.login3.service.JwtService;
import com.example.login3.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

		@Autowired
		private RefreshTokenRepository refreshTokenRepository;

		@Autowired
		private JwtService jwtService;

		@Override
		public void saveToken(Map<String, String> map, User authUser) {
				String token = map.get("refreshToken");
				Instant expiry = jwtService.extractClaim(token, Claims::getExpiration).toInstant();

				Optional<RefreshToken> existing = refreshTokenRepository.findByUser(authUser);
        if (existing.isPresent()) {
            RefreshToken rfToken = existing.get();
            rfToken.setToken(token);
            rfToken.setExpiryDate(expiry);
            refreshTokenRepository.save(rfToken);
        } else {
            refreshTokenRepository.save(new RefreshToken(null, authUser, token, expiry));
        }
		}

		@Override
		public boolean verifyToken(String token) {
				RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElse(new RefreshToken());
				return jwtService.isTokenExpired(refreshToken);
		}

		@Override
		public void deleteToken(String token) {
				refreshTokenRepository.deleteByToken(token);
		}
}
