package com.kshz.fakebookserver.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.kshz.fakebookserver.annotation.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value != null) {
			String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$";
			Pattern passwordPattern = Pattern.compile(passwordRegex);
			Matcher passwordMatcher = passwordPattern.matcher(value);
			return passwordMatcher.matches();
		} else {
			return true;
		}
	}

}
