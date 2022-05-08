package com.kshz.fakebookserver.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kshz.fakebookserver.exceptions.BadRequestException;
import com.kshz.fakebookserver.jwt.JWT;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.response.AuthResponse;
import com.kshz.fakebookserver.service.RegisterationCheckService;
import com.kshz.fakebookserver.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JWT jwt;
	
	@Autowired
	private RegisterationCheckService registerationCheckService;
	
	@PostMapping("/google-login")
	public AuthResponse loginWithGoogle(@RequestBody String credential) {
		User authenticatedUser = userService.loginWithGoogle(credential);
		
		String jwtToken = jwt.generateToken(authenticatedUser.getId(),
				authenticatedUser.getName(), 
				authenticatedUser.getUsername(), 
				authenticatedUser.getEmail());
		
		return new AuthResponse(authenticatedUser, jwtToken);
	}

	@PostMapping("/register")
	@ResponseStatus(code = HttpStatus.CREATED)
	public AuthResponse registerUser(@Valid @RequestBody User newUser) {
		User savedUser = userService.save(newUser);
		
		String jwtToken = jwt.generateToken(savedUser.getId(), 
				savedUser.getName(), 
				savedUser.getUsername(),
				savedUser.getEmail());
		
		return new AuthResponse(savedUser, jwtToken);
	}

	@PostMapping("/login")
	public AuthResponse loginUser(@RequestBody Map<String, String> loginCredentials) {
		String usernameOrEmail = "";
		
		String username = loginCredentials.get("username");
		String password = loginCredentials.get("password");
		String email = loginCredentials.get("email");
		
		if (username != null) {
			usernameOrEmail = "username=" + username;
		} else if (email != null) {
			usernameOrEmail = "email=" + email ;
		} else {
			if (password == null) {
				throw new BadRequestException("Please provide username/email and password", 
						"username/email and password must be defined");
			} else {
				throw new BadRequestException("Please provide username/email", 
						"username/email must be defined");
			}
		}

		User user = userService.loginUser(usernameOrEmail, password);
		
		String jwtToken = jwt.generateToken(user.getId(), 
				user.getName(), 
				user.getUsername(),
				user.getEmail());
		
		return new AuthResponse(user, jwtToken);
	}
	
	@PostMapping("/users")
	public Map<String, Boolean> checkIfRegistered(@RequestBody Map<String, String> requestBody, 
			HttpServletRequest request) {
		String license = request.getHeader("fyb");
		Map<String, Boolean> result = new HashMap<>();
		result.put("registered", registerationCheckService.isRegistered(license, requestBody));
		return result;
	}
}
