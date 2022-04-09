package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public class AccessOrModificationNotAllowedException extends FakeBookApplicationException {

	private static final long serialVersionUID = 1175858032873170592L;

	public AccessOrModificationNotAllowedException(String reason, String details) {
		super(HttpStatus.FORBIDDEN, reason, details);
	}

}
