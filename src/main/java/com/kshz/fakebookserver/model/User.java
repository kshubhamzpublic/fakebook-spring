package com.kshz.fakebookserver.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.kshz.fakebookserver.annotation.Password;

@Document("users")
public class User {
	@Id
	@JsonIgnore
	private String id;

	@Field
	@NotNull(message = "username must be defined")
	@NotBlank(message = "Please provide username")
	@Indexed(unique = true, name = "username_1")
	@Pattern(regexp = "^[a-z][a-z0-9\\._-]{3,20}$", message = "Please provide a valid username")
	private String username;

	@Field
	@NotNull(message = "name must be defined")
	@NotBlank(message = "Please provide name")
	private String name;

	@Field
	@NotNull(message = "email must be defined")
	@NotBlank(message = "Please provide email")
	@Indexed(unique = true, name = "email_1")
	@Pattern(regexp = "^[a-z][a-z0-9\\._-]+[@][a-z]+[.][a-z]+$", message = "Please provide valid email address")
	private String email;

	@Field
	@NotNull(message = "password must be defined")
	@NotBlank(message = "Please provide password")
	@JsonProperty(access = Access.WRITE_ONLY)
	@Password
	private String password;

	@Field
	@Size(max = 50, message = "Description can't be greater than 50 characters.")
	private String description;

	@Field
	private boolean social;

	@Field
	private String profileImage;

	public User() {
		super();
	}

	public User(String username, String name, String email, String password, String description) {
		super();
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSocial() {
		return social;
	}

	public void setSocial(boolean social) {
		this.social = social;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", name=" + name + ", email=" + email + ", description="
				+ description + "]";
	}

}
