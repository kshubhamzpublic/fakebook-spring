package com.kshz.fakebookserver.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.kshz.fakebookserver.annotation.Password;

public class UpdateUserRequest {
	@Pattern(regexp = "^[a-z][a-z0-9\\._-]{3,20}$", 
			message = "Please provide a valid username")
	private String username;
	
	private String name;
	
	@Pattern(regexp = "^[a-z][a-z0-9\\._-]+[@][a-z]+[.][a-z]+$", 
			message = "Please provide valid email address")
	private String email;
	
	private String currentPassword;
	
	@Password(message = "Not a strong new password")
	private String newPassword;
	
	@Size(max = 50, message = "Description can't be greater than 50 characters.")
	private String description;
	
	public UpdateUserRequest() {
		
	}

	public UpdateUserRequest(String username, String name, String email, String currentPassword, String newPassword, String description) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.description = description;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UpdateUserRequest [username=" + username + ", name=" + name + ", email=" + email + "]";
	}
	
}
