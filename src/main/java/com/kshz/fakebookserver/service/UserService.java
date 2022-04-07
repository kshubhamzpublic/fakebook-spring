package com.kshz.fakebookserver.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.repository.UserRepository;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<User> findById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Override
	public User save(User user) {
		// hash password
		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		
		// update password with hashed password
		user.setPassword(hashedPassword);
		
		return userRepository.save(user);
	}

}
