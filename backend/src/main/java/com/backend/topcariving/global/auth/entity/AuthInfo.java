package com.backend.topcariving.global.auth.entity;

import java.time.LocalDateTime;

import com.backend.topcariving.global.auth.entity.enums.LoginType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthInfo {

	private Long authInfoId;

	private String refreshToken;

	private LocalDateTime expiredTime;

	private LoginType loginType;

	// FK
	private Long userId;

	public void updateToken(String refreshToken, LocalDateTime expiredTime) {
		this.refreshToken = refreshToken;
		this.expiredTime = expiredTime;
	}
}
