package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends FakeBookApplicationException {

	private static final long serialVersionUID = -2697925998510913586L;

	public EntityNotFoundException(String reason, String details) {
		super(HttpStatus.NOT_FOUND, reason, details);
	}

}
