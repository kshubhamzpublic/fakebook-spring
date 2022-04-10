package com.kshz.fakebookserver.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kshz.fakebookserver.exceptions.BadRequestException;
import com.kshz.fakebookserver.exceptions.ConfigurationException;

@Service
public class RegisterationCheckService {
	
	@Value("${registration.license}")
	private String license;
	
	@Autowired
	private UserService userService;
	
	public boolean isRegistered(String clientLicense, Map<String, String> requestBody) {
		if (license == null) {
			throw new ConfigurationException("Cannot proccess this request.", "Registration License is undefined.");
		}
		
		if (clientLicense == null) {
			throw new BadRequestException("Cannot proccess this request.", "Registration License is not provided.");
		}
		
		if (!clientLicense.equals(license)) {
			throw new BadRequestException("Cannot proccess this request.", "Registration License provided is invalid.");
		}
		
		String username = requestBody.get("username");
		String email = requestBody.get("email");
		if(username != null) {
			return userService.findUserByUsername(username).isPresent();
		} else if (email != null) {
			return userService.findUserByEmail(email).isPresent();
		} else {
			throw new BadRequestException("Cannot proccess this request.", "Must provide either username or email.");
		}
	}
}
