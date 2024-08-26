package com.example.login3.service;

import com.example.login3.model.User;
import com.example.login3.model.token.RefreshToken;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

		public Map<String, String> generateNewTokenMap(User user);

		public String generateNewAccessToken(String refreshToken);
		public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

		public boolean isTokenExpired(RefreshToken token);

}
