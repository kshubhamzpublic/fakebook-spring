package com.kshz.fakebookserver.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExceptionResponse {
	private String message;
	private List<String> details;
	private LocalDateTime timeStamp;

	public ExceptionResponse(String message) {
		super();
		this.message = message;
		this.timeStamp = LocalDateTime.now();
		this.details = new ArrayList<>();
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	// add details
	public void addDetails(String msg) {
		details.add(msg);
	}

	@Override
	public String toString() {
		return "ExceptionResponse [message=" + message + ", timeStamp=" + timeStamp + "]";
	}

}
