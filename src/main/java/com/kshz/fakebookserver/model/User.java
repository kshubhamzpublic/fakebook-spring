package com.kshz.fakebookserver.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Document("user")
public class User {
	@Id
	private String id;
	
	@Field
	@NotNull(message = "username must be defined")
	@NotBlank(message = "Please provide username")
	@Indexed(unique = true)
	private String username;
	
	@Field
	@NotNull(message = "name must be defined")
	@NotBlank(message = "Please provide name")
	private String name;
	
	@Field
	@NotNull(message = "email must be defined")
	@NotBlank(message = "Please provide email")
	@Indexed(unique = true)
	private String email;
	
	@Field
	@NotNull(message = "password must be defined")
	@NotBlank(message = "Please provide password")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Field
	private String description;
	
	private String token;
	
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
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", name=" + name + ", email=" + email + ", description="
				+ description + "]";
	}

}
