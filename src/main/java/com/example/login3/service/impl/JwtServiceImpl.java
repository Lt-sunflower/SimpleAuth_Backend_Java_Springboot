package com.example.login3.service.impl;

import com.example.login3.model.User;
import com.example.login3.model.token.RefreshToken;
import com.example.login3.repository.RefreshTokenRepository;
import com.example.login3.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

		@Autowired
		private RefreshTokenRepository refreshTokenRepository;

		@Value("${application.security.jwt.secretKey}")
		private String SECRET_KEY;
		@Value("${application.security.jwt.accessExpiration}")
		private int ACCESS_TOKEN_EXPIRY;
		@Value("${application.security.jwt.refreshExpiration}")
		private int REFRESH_TOKEN_EXPIRY;

		public Map<String, String> generateNewTokenMap(User user) {

				Map<String, String> tokens = new HashMap<>();
				tokens.put("accessToken", Jwts
						.builder()
						.subject(user.getUsername())
						.issuedAt(new Date(System.currentTimeMillis()))
						.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY * 1000L))
						.signWith(getSigningKey())
						.compact());

				tokens.put("refreshToken", Jwts
						.builder()
						.subject(user.getUsername())
						.issuedAt(new Date(System.currentTimeMillis()))
						.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY * 1000L))
						.signWith(getSigningKey())
						.compact());

				return tokens;
		}

		public String generateNewAccessToken(String refreshToken) {

				String username = extractClaim(refreshToken, Claims::getSubject);
				return Jwts
						.builder()
						.subject(username)
						.issuedAt(new Date(System.currentTimeMillis()))
						.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY * 1000L))
						.signWith(getSigningKey())
						.compact();
		}

		public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
				try {
						return claimsResolver.apply(extractAllClaims(token));
				} catch (Exception e) {
						log.info("Exception: " + e.getMessage());
						return null;
				}
		}

		public boolean isTokenExpired(RefreshToken token) {
				if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
						return false;
				}
				return true;
		}

		private Claims extractAllClaims(String jwt) throws SignatureException {

				return Jwts
						.parser()
						.verifyWith(getSigningKey())
						.build()
						.parseSignedClaims(jwt)
						.getPayload();
		}

		private SecretKey getSigningKey() {

				byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
				return Keys.hmacShaKeyFor(keyBytes);
		}

}
