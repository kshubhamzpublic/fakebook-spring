package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public class DetailsMismatchException extends FakeBookApplicationException {

	private static final long serialVersionUID = 6964736388210973435L;

	public DetailsMismatchException(String reason, String details) {
		super(HttpStatus.FORBIDDEN, reason, details);
	}

}
