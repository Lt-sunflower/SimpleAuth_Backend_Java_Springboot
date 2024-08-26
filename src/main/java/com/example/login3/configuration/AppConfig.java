package com.example.login3.configuration;

import com.example.login3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

		@Autowired
		private UserRepository userRepository;

		@Bean
		public UserDetailsService userDetailsService() {
				return username -> userRepository.findByUsernameIgnoreCase(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found in DB"));
		}

		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
				throws Exception {
				return config.getAuthenticationManager();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
				return new BCryptPasswordEncoder();
		}

		@Bean
		public WebMvcConfigurer corsConfigurer() {
				return new WebMvcConfigurer() {
						@Override
						public void addCorsMappings(CorsRegistry registry) {
								registry.addMapping("/**")
										.allowedOrigins("http://localhost:3000")
										.allowCredentials(true);
						}
				};
		}

}
