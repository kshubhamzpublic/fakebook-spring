package com.kshz.fakebookserver.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatePostRequest {

	@Size(max = 250, message = "Message cannot be greater than 250 characters.")
	private String message;

	private boolean anonymous;

	@JsonProperty("public")
	private boolean publicPost;
	
	@NotEmpty(message = "type property must be defined")
	private String type;

	public UpdatePostRequest() {
	}

	public UpdatePostRequest(String message, boolean anonymous, boolean publicPost, String type) {
		this.message = message;
		this.anonymous = anonymous;
		this.publicPost = publicPost;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "UpdatePostRequest [message=" + message + ", anonymous=" + anonymous + ", publicPost=" + publicPost
				+ "]";
	}

}
