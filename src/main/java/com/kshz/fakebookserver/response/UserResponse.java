package com.kshz.fakebookserver.response;

import java.util.List;

import com.kshz.fakebookserver.model.Post;
import com.kshz.fakebookserver.model.User;

public class UserResponse {
	private User user;
	private List<Post> posts;
	private boolean self;
	private String message;

	public UserResponse() {
	}

	public UserResponse(User user, List<Post> posts, boolean self, String message) {
		this.user = user;
		this.posts = posts;
		this.self = self;
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "UserResponse [user=" + user + ", posts=" + posts + ", self=" + self + ", message=" + message + "]";
	}

}
