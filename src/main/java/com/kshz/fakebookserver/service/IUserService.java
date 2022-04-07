package com.kshz.fakebookserver.service;

import java.util.Optional;

import com.kshz.fakebookserver.model.User;

public interface IUserService {
	public Optional<User> findById(String id);

	public Optional<User> findUserByUsername(String username);

	public Optional<User> findUserByEmail(String email);
	
	public User save(User user);
}
