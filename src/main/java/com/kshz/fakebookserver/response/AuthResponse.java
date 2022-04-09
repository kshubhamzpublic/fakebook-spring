package com.kshz.fakebookserver.response;

import org.springframework.stereotype.Component;

import com.kshz.fakebookserver.model.User;

@Component
public class AuthResponse {
	private String token;
	private String username;
	private String name;
	
	public AuthResponse() {
	}

	public AuthResponse(String token, String username, String name) {
		this.token = token;
		this.username = username;
		this.name = name;
	}

	public AuthResponse(User user, String token) {
		this.name = user.getName();
		this.username = user.getUsername();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AuthResponse [username=" + username + ", name=" + name + "]";
	}
}
