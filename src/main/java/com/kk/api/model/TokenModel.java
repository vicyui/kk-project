package com.kk.api.model;

import com.kk.api.app.entity.User;
import lombok.Data;

@Data
public class TokenModel {
	private Long userId;
	private String token;
	private User user;

	public TokenModel() {
	}

	public TokenModel(User user) {
		this.user = user;
	}

	public TokenModel(User user,String token) {
		this.user = user;
		this.token = token;
	}
}
