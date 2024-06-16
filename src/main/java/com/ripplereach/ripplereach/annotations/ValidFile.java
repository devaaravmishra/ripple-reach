package com.ripplereach.ripplereach.annotations;

import com.ripplereach.ripplereach.annotations.validators.MultipartFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MultipartFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {
    String message() default "Invalid file";

    long maxSize() default 5048576; // Default max size 1MB

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
