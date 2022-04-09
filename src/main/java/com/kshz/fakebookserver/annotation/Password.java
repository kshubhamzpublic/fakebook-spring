package com.kshz.fakebookserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.kshz.fakebookserver.validator.PasswordValidator;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
	String message() default "Not a strong password";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
