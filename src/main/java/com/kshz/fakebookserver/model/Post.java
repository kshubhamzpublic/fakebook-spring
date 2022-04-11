package com.kshz.fakebookserver.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document("posts")
public class Post {
	@Id
	@JsonProperty("_id")
	private String id;

	@Field
	@Size(max = 250, message = "Message cannot be greater than 250 characters.")
	@NotEmpty(message = "Message must be defined.")
	private String message;

	@Field
	@NotNull(message = "Anonymous must be defined.")
	private boolean anonymous;

	@JsonProperty("public")
	@Field(name = "public")
	@NotNull(message = "Public must be defined.")
	private boolean publicPost;

	@Field
	private LocalDateTime postedAt;
	
	@Field
	@DocumentReference(collection = "users")
	private User author;

	@Field
	@DocumentReference(collection = "users", lazy = true)
	private Set<User> likes;

	@Field
	@DocumentReference(collection = "users", lazy = true)
	private Set<User> dislikes;

	public Post() {
	}

	public Post(String message, boolean anonymous, boolean publicPost) {
		this.message = message;
		this.anonymous = anonymous;
		this.publicPost = publicPost;
	}
	
	public void initializePost() {
		this.postedAt = LocalDateTime.now();
		this.likes = new TreeSet<>();
		this.dislikes = new TreeSet<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public boolean isPublicPost() {
		return publicPost;
	}

	public void setPublicPost(boolean publicPost) {
		this.publicPost = publicPost;
	}

	public LocalDateTime getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDateTime postedAt) {
		this.postedAt = postedAt;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Set<User> getLikes() {
		return likes;
	}

	public void setLikes(Set<User> likes) {
		this.likes = likes;
	}

	public Set<User> getDislikes() {
		return dislikes;
	}

	public void setDislikes(Set<User> dislikes) {
		this.dislikes = dislikes;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", message=" + message + ", anonymous=" + anonymous + ", publicPost=" + publicPost
				+ ", postedAt=" + postedAt + ", author=" + author + ", likes=" + likes + ", dislikes=" + dislikes + "]";
	}

}
