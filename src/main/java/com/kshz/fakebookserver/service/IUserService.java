package com.kshz.fakebookserver.service;

import java.util.List;
import java.util.Optional;

import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.request.UpdateUserRequest;

public interface IUserService {
	public Optional<User> findById(String id);

	public Optional<User> findUserByUsername(String username);

	public Optional<User> findUserByEmail(String email);

	public User save(User user);

	public User loginUser(String usernameOrEmail, String password);

	public User updateUser(String clientId, UpdateUserRequest requestBody);
	
	public User loginWithGoogle(String credential);
	
	public List<User> findByName(String name);
}
