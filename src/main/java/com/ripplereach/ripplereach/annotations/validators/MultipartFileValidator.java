package com.ripplereach.ripplereach.annotations.validators;

import com.ripplereach.ripplereach.annotations.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private long maxSize;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        return file.getSize() <= maxSize;
    }
}
