package com.example.login3.configuration;

import com.example.login3.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

		@Autowired
		private RefreshTokenService refreshTokenService;

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
				List<Cookie> cookies = List.of(request.getCookies());
				cookies = cookies.stream().filter(cookie -> cookie.getName().equals("refreshToken")).toList();

				if (cookies.size() > 0) {
						Cookie refreshTokenCookie = cookies.get(0);
						String refreshToken = refreshTokenCookie.getValue();
						refreshTokenService.deleteToken(refreshToken);
						refreshTokenCookie.setMaxAge(0);
						response.addCookie(refreshTokenCookie);
				}

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().flush();
		}
}
