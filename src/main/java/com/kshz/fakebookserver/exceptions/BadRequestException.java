package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends FakeBookApplicationException {

	private static final long serialVersionUID = 7291025830263096360L;

	public BadRequestException(String reason, String details) {
		super(HttpStatus.BAD_REQUEST, reason, details);
	}

}
