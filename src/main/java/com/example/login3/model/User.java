package com.example.login3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements UserDetails {

		@Id
		@GeneratedValue
		private Integer id;

		@Column(nullable = false, unique = true)
		private String username;

		@Column(nullable = false)
		private String password;

		@Enumerated(EnumType.STRING)
		private Role role;

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
				return List.of(new SimpleGrantedAuthority(role.name()));
		}

}
