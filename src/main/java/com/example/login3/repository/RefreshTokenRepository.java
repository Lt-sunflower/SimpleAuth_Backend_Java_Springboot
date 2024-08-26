package com.example.login3.repository;

import com.example.login3.model.User;
import com.example.login3.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

		Optional<RefreshToken> findByToken(String token);

		Optional<RefreshToken> findByUser(User authUser);

		void deleteByToken(String token);
}
