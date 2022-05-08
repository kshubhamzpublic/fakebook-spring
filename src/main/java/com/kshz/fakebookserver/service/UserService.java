package com.kshz.fakebookserver.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.kshz.fakebookserver.exceptions.AuthorizationException;
import com.kshz.fakebookserver.exceptions.BadRequestException;
import com.kshz.fakebookserver.exceptions.DetailsMismatchException;
import com.kshz.fakebookserver.exceptions.EntityNotFoundException;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.repository.UserRepository;
import com.kshz.fakebookserver.request.UpdateUserRequest;

@Service
public class UserService implements IUserService {
	
	@Value("${bcrypt.salt.rounds}")
	private int saltRounds;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GoogleTokenVerifer googleVerifier;
	
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
		if (!user.isSocial()) {
			// hash password
			String hashedPassword = hashPassword(user.getPassword());

			// update password with hashed password
			user.setPassword(hashedPassword);
		}

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
		
		// bad request if socials authenticated user tries to sign-in with username/email and password
		if (user.get().isSocial()) {
			throw new BadRequestException("You are registered with your social account.", null);
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
		
		if (!user.isSocial() && isUpdatingValueValid(newEmail)) {
			user.setEmail(newEmail);
		}
		
		if (isUpdatingValueValid(newDescription)) {
			user.setDescription(newDescription);
		}
		
		if (!user.isSocial() && isUpdatingValueValid(currentPassword) && isUpdatingValueValid(newPassword)) {
			// verifying currentPassword
			boolean isCurrentPasswordValid = BCrypt.checkpw(currentPassword, user.getPassword());
			
			if (!isCurrentPasswordValid) {
				throw new AuthorizationException("Current Password is invalid/not-matching.", null);
			}
			
			user.setPassword(hashPassword(newPassword));
		}
		
		return userRepository.save(user);
	}

	private boolean isUpdatingValueValid(String value) {
		return value != null && value.trim().length() > 1;
	}
	
	private String hashPassword(String pw) {
		String salt = BCrypt.gensalt(saltRounds);
		
		return BCrypt.hashpw(pw, salt);
	}

	@Override
	public List<User> findByName(String name) {
		return userRepository.findByName(name);
	}

	@Override
	public User loginWithGoogle(String credential) {
		if (!googleVerifier.isTokenValid(credential)) {
			throw new AuthorizationException("Invalid Token", null);
		}
		
		Payload payload = googleVerifier.getPayload(credential);
		
		System.out.println(payload);
		
		// extract data from payload
		final String email = (String) payload.get("email");
		final String name = (String) payload.get("name");
		final String picture = (String) payload.get("picture");
		final String username = email.split("@")[0].trim();
		
		// if user doesn't exist
		Optional<User> googleAuthenticatedUser = findUserByEmail(email);
		
		if (googleAuthenticatedUser.isEmpty()) {
			// create new user and set property
			User newUser = new User();
			newUser.setSocial(true);
			newUser.setName(name);
			newUser.setEmail(email);
			newUser.setUsername(username);
			newUser.setProfileImage(picture);
			return save(newUser);
		} else {
			User user = googleAuthenticatedUser.get();
			if (!picture.equals(user.getProfileImage())) {
				user.setProfileImage(picture);
				return save(user);
			}
			return user;
		}
	}
	
}
