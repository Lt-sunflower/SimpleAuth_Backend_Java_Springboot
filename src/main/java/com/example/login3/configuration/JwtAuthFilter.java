package com.example.login3.configuration;

import com.example.login3.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private JwtService jwtService;

		private String BEARER_KEY_WORD = "Bearer ";

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {

				final String authHeader = request.getHeader("Authorization");

				if (authHeader == null || !authHeader.startsWith(BEARER_KEY_WORD)) {
						filterChain.doFilter(request, response);
						return;
				} else {
						String jwt = authHeader.replace(BEARER_KEY_WORD, "");
						String username = jwtService.extractClaim(jwt, Claims::getSubject);

						if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
								try {
										UserDetails userDetails = userDetailsService.loadUserByUsername(username);

										UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
												null, userDetails.getAuthorities());
										authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
										SecurityContextHolder.getContext().setAuthentication(authToken);

								} catch (Exception e) {
										log.info("Exception: " + e.getMessage());
										filterChain.doFilter(request, response);
										return;
								}

						}
				}
				filterChain.doFilter(request, response);
		}
}
