package com.kshz.fakebookserver.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kshz.fakebookserver.exceptions.EntityNotFoundException;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.request.UpdateUserRequest;
import com.kshz.fakebookserver.response.UserResponse;
import com.kshz.fakebookserver.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping("/{username}")
	public UserResponse getUserWithId(@PathVariable String username, HttpServletRequest req) {
		String clientId = (String) req.getAttribute("userId");
		Optional<User> user = userService.findUserByUsername(username);

		if (user.isEmpty()) {
			throw new EntityNotFoundException("No user is available with username: " + username, null);
		}

		return new UserResponse(user.get(), null, clientId.equals(user.get().getId()), null);
	}

	@PatchMapping
	public UserResponse updateUser(@Valid @RequestBody UpdateUserRequest requestBody, HttpServletRequest req) {
		String clientId = (String) req.getAttribute("userId");
		
		User updatedUser = userService.updateUser(clientId, requestBody);

		return new UserResponse(updatedUser, null, updatedUser.getId().equals(clientId), "User updated successfully");
	}

}
