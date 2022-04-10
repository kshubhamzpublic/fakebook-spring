package com.kshz.fakebookserver.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.kshz.fakebookserver.exceptions.AuthorizationException;
import com.kshz.fakebookserver.exceptions.DetailsMismatchException;
import com.kshz.fakebookserver.exceptions.EntityNotFoundException;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.repository.UserRepository;
import com.kshz.fakebookserver.request.UpdateUserRequest;

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

	@Override
	public User loginUser(String usernameOrEmail, String password) {
		Optional<User> user = null;
		if (usernameOrEmail.contains("username=")) {
			String username = usernameOrEmail.replace("username=", "").trim();
			user = findUserByUsername(username);
		} else {
			String email = usernameOrEmail.replace("email=", "").trim();
			user = findUserByEmail(email);
		}
		
		// when there's no user with provided username/email
		if (user.isEmpty()) {
			throw new DetailsMismatchException("Incorrect combination of email/username and password", null);
		}
		
		// compare password
		boolean isPasswordCorrect = BCrypt.checkpw(password, user.get().getPassword());
		
		// when password doesn't match
		if (!isPasswordCorrect) {
			throw new DetailsMismatchException("Incorrect combination of email/username and password", null);
		}

		return user.get();
	}

	@Override
	public User updateUser(String clientId, UpdateUserRequest requestBody) {
		Optional<User> currentUser = findById(clientId);
		if (currentUser.isEmpty()) {
			throw new EntityNotFoundException("User doesn't exist with id: " + clientId, null);
		}
		
		User user = currentUser.get();
		
		// extract request body
		String newUsername = requestBody.getUsername();
		String newName = requestBody.getName();
		String newEmail = requestBody.getEmail();
		String newDescription = requestBody.getDescription();
		String newPassword = requestBody.getNewPassword();
		String currentPassword = requestBody.getCurrentPassword();
		
		// update property
		
		if (isUpdatingValueValid(newUsername)) {
			user.setUsername(newUsername);
		}
		
		if (isUpdatingValueValid(newName)) {
			user.setName(newName);
		}
		
		if (isUpdatingValueValid(newEmail)) {
			user.setEmail(newEmail);
		}
		
		if (isUpdatingValueValid(newDescription)) {
			user.setDescription(newDescription);
		}
		
		if (isUpdatingValueValid(currentPassword) && isUpdatingValueValid(newPassword)) {
			// verifying currentPassword
			boolean isCurrentPasswordValid = BCrypt.checkpw(currentPassword, user.getPassword());
			
			if (!isCurrentPasswordValid) {
				throw new AuthorizationException("Current Password is invalid/not-matching.", null);
			}
			
			user.setPassword(newPassword);
		}
		
		return save(user);
	}

	private boolean isUpdatingValueValid(String value) {
		return value != null && value.trim().length() > 1;
	}

	@Override
	public List<User> findByName(String name) {
		return userRepository.findByName(name);
	}
	
}
