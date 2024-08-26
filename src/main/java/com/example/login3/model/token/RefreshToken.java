package com.example.login3.model.token;

import com.example.login3.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RefreshToken")
public class RefreshToken {

		@Id
		@GeneratedValue
		private Integer id;

		@OneToOne
		@JoinColumn(name = "user_id", referencedColumnName = "id")
		private User user;

		@Column(nullable = false, unique = true)
		private String token;

		@Column(nullable = false)
		private Instant expiryDate;
}
