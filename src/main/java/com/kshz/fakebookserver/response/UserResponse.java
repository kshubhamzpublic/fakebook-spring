package com.kshz.fakebookserver.response;

import java.util.List;
import java.util.stream.Collectors;

import com.kshz.fakebookserver.model.Post;
import com.kshz.fakebookserver.model.User;

public class UserResponse {
	private User user;
	private List<PostResponse> posts;
	private boolean self;
	private String message;

	public UserResponse() {
	}

	public UserResponse(User user, List<Post> posts, boolean self, String message) {
		this.user = user;
		
		this.posts = posts.stream()
				.map(post -> new PostResponse(post, self))
				.collect(Collectors.toList());
		
		this.self = self;
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<PostResponse> getPosts() {
		return posts;
	}

	public void setPosts(List<PostResponse> posts) {
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
