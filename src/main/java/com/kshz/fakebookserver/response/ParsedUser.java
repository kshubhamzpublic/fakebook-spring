package com.kshz.fakebookserver.response;

import com.kshz.fakebookserver.model.User;

public class ParsedUser {
	private String username;
	private String name;

	public ParsedUser(User user, boolean anonymousFlag, boolean selfFlag) {
		if (anonymousFlag && !selfFlag) {
			this.username = null;
			this.name = "Anonymous";
		} else {
			this.username = user.getUsername();
			this.name = user.getName();
		}
	}
	
	public ParsedUser(User user) {
		this.username = user.getUsername();
		this.name = user.getName();
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
		return "ParsedUser [username=" + username + ", name=" + name + "]";
	}
}
