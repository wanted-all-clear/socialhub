package com.allclear.socialhub.user.domain;

import java.time.LocalDateTime;

import com.allclear.socialhub.common.domain.Timestamped;
import com.allclear.socialhub.user.type.UserCertifyStatus;
import com.allclear.socialhub.user.type.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private LocalDateTime deletedAt;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserCertifyStatus certifyStatus;
}
