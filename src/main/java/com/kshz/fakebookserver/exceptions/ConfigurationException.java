package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public class ConfigurationException extends FakeBookApplicationException {

	private static final long serialVersionUID = -8477160224762877589L;

	public ConfigurationException(String reason, String details) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, reason, details);
		// TODO Auto-generated constructor stub
	}

}
