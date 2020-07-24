package com.okboss.api.model;

import com.okboss.api.app.entity.User;
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
