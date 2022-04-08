package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends FakeBookApplicationException {

	private static final long serialVersionUID = -2093598962027666327L;

	public AuthorizationException(String reason, String details) {
		super(HttpStatus.UNAUTHORIZED, reason, details);
	}

}
