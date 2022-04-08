package com.kshz.fakebookserver.exceptions;

import org.springframework.http.HttpStatus;

public abstract class FakeBookApplicationException extends RuntimeException {
	private static final long serialVersionUID = 2452827898554915381L;

	protected HttpStatus statusCode;
	protected String reason;
	protected String details;

	protected FakeBookApplicationException(HttpStatus statusCode, String reason, String details) {
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.details = details;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public String getReason() {
		return reason;
	}

	public String getDetails() {
		return details;
	}
}
