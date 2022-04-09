package com.kshz.fakebookserver.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.kshz.fakebookserver.exceptions.EntityNotFoundException;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.request.UpdateUserRequest;
import com.kshz.fakebookserver.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	private SimpleBeanPropertyFilter propertyFilter = SimpleBeanPropertyFilter
			.filterOutAllExcept("id", "username", "name", "email", "description");
	private FilterProvider filterProvider = new SimpleFilterProvider()
			.addFilter("filterUserProperties", propertyFilter);

	@GetMapping("/{userId}")
	public MappingJacksonValue getUserWithId(@PathVariable String userId) {
		Optional<User> user = userService.findById(userId);

		if (user.isEmpty()) {
			throw new EntityNotFoundException("No user is available with id: " + userId, null);
		}

		// filtering property "token" and sending rest property
		MappingJacksonValue mappings = new MappingJacksonValue(user.get());
		mappings.setFilters(filterProvider);

		return mappings;
	}

	@PatchMapping("/{userId}")
	public MappingJacksonValue updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserRequest requestBody,
			HttpServletRequest req) {
		String clientId = (String) req.getAttribute("userId");
		
		User updatedUser = userService.updateUser(clientId, userId, requestBody);
		
		// filtering property "token" and sending rest property
		MappingJacksonValue mappings = new MappingJacksonValue(updatedUser);
		mappings.setFilters(filterProvider);

		return mappings;
	}

}
