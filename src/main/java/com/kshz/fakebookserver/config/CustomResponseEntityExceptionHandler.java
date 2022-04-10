package com.kshz.fakebookserver.config;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kshz.fakebookserver.exceptions.ExceptionResponse;
import com.kshz.fakebookserver.exceptions.FakeBookApplicationException;
import com.kshz.fakebookserver.utils.StringParser;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// create exception response object
		ExceptionResponse exRes = new ExceptionResponse("Invalid Fields");

		// extract messages from field errors and add it to details
		Set<String> invalidFields = new TreeSet<>();
		List<FieldError> fieldErrors = ex.getFieldErrors();
		for (FieldError fErr : fieldErrors) {
			exRes.addDetails(fErr.getDefaultMessage());
			invalidFields.add(fErr.getField());
		}

		// set message on exRes
		if (!invalidFields.isEmpty()) {
			exRes.setMessage("Invalid Fields: " + invalidFields);
		}

		return new ResponseEntity<>(exRes, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exRes = new ExceptionResponse("Cannot parse request body / http message");
		
		try {
			exRes.addDetails(StringParser
					.parseHttpMessageNotReadableMostSpecificCause(ex.getMostSpecificCause()
							.getLocalizedMessage()));
		} catch (IndexOutOfBoundsException e) {
			exRes.addDetails(ex.getMostSpecificCause().getLocalizedMessage());
		} catch (Exception e) {
			exRes.addDetails(ex.getLocalizedMessage());
		}
		
		return new ResponseEntity<>(exRes, status);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest req) {
		ExceptionResponse exRes = new ExceptionResponse("Something went wrong");
		return new ResponseEntity<ExceptionResponse>(exRes, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DuplicateKeyException.class)
	public final ResponseEntity<ExceptionResponse> handleDuplicateKeyException(DuplicateKeyException ex,
			WebRequest req) {
		ex.printStackTrace();
		ExceptionResponse exRes = null;

		try {
			// parse Exception to get custom message
			String[] customDuplicateKeyParsedError = StringParser
					.parseDuplicateKeyErrorRootCause(ex.getRootCause().getLocalizedMessage());
			exRes = new ExceptionResponse(customDuplicateKeyParsedError[0] + " must be unique");
			exRes.addDetails(customDuplicateKeyParsedError[1]);
		} catch (IndexOutOfBoundsException e) {
			exRes = new ExceptionResponse("Unique Key violation.");
		} catch (Exception e) {
			exRes = new ExceptionResponse("Unique Key violated.");
			exRes.addDetails("Error in parsing UniqueKeyViolation Exception");
		}

		return new ResponseEntity<ExceptionResponse>(exRes, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(FakeBookApplicationException.class)
	public final ResponseEntity<ExceptionResponse> handleApplicationException(FakeBookApplicationException ex,
			WebRequest req) {
		HttpStatus statusCode = ex.getStatusCode();

		ExceptionResponse exRes = new ExceptionResponse(ex.getReason());
		if (ex.getDetails() != null) {
			exRes.addDetails(ex.getDetails());	
		}

		return new ResponseEntity<ExceptionResponse>(exRes, statusCode);
	}
}
