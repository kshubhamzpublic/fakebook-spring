package com.kshz.fakebookserver.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshz.fakebookserver.model.Post;

public class PostResponse {
	private String _id;
	private String message;
	private boolean anonymous;

	@JsonProperty("public")
	private boolean publicPost;

	private ParsedUser author;
	private List<ParsedUser> likes;
	private List<ParsedUser> dislikes;
	private LocalDateTime postedAt;

	public PostResponse(Post post, boolean selfFlag) {
		this._id = post.getId();
		this.message = post.getMessage();
		this.anonymous = post.isAnonymous();
		this.publicPost = post.isPublicPost();
		this.postedAt = post.getPostedAt();
		this.author = new ParsedUser(post.getAuthor(), anonymous, selfFlag);
		
		// map each value in likes and dislikes set to ParsedUser via stream api
		this.likes = post.getLikes()
				.stream()
				.map(user -> new ParsedUser(user))
				.collect(Collectors.toList());
		this.dislikes = post.getDislikes()
				.stream()
				.map(user -> new ParsedUser(user))
				.collect(Collectors.toList());
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

	public ParsedUser getAuthor() {
		return author;
	}

	public void setAuthor(ParsedUser author) {
		this.author = author;
	}

	public List<ParsedUser> getLikes() {
		return likes;
	}

	public void setLikes(List<ParsedUser> likes) {
		this.likes = likes;
	}

	public List<ParsedUser> getDislikes() {
		return dislikes;
	}

	public void setDislikes(List<ParsedUser> dislikes) {
		this.dislikes = dislikes;
	}

	public LocalDateTime getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDateTime postedAt) {
		this.postedAt = postedAt;
	}

	@Override
	public String toString() {
		return "PostResponse [_id=" + _id + ", message=" + message + ", anonymous=" + anonymous + ", publicPost="
				+ publicPost + ", author=" + author + ", likes=" + likes + ", dislikes=" + dislikes + "]";
	}

}
