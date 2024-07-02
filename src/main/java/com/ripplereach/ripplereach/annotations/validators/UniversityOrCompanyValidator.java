package com.ripplereach.ripplereach.annotations.validators;

import com.ripplereach.ripplereach.annotations.UniversityOrCompany;
import com.ripplereach.ripplereach.dtos.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniversityOrCompanyValidator
    implements ConstraintValidator<UniversityOrCompany, RegisterRequest> {
  @Override
  public boolean isValid(RegisterRequest dto, ConstraintValidatorContext context) {
    boolean isUniversityPresent = dto.getUniversity() != null && !dto.getUniversity().isEmpty();
    boolean isCompanyPresent = dto.getCompany() != null && !dto.getCompany().isEmpty();

    // Only one of them should be present
    boolean isValid = isUniversityPresent != isCompanyPresent;

    // If request is invalid attaching proper key "validation" to the error
    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
          .addPropertyNode("universityOrCompany")
          .addConstraintViolation();
    }

    return isValid;
  }
}
