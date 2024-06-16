package com.ripplereach.ripplereach.annotations.validators;

import com.ripplereach.ripplereach.annotations.CompanyAndProfession;
import com.ripplereach.ripplereach.dtos.RegisterRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyAndProfessionValidator
    implements ConstraintValidator<CompanyAndProfession, RegisterRequestDto> {
  @Override
  public boolean isValid(RegisterRequestDto dto, ConstraintValidatorContext context) {
    boolean companyAndNotProfession = dto.getCompany() != null && dto.getCompany().isEmpty();

    if (companyAndNotProfession) {
      // If request is invalid attaching proper key "validation" to the error
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
          .addPropertyNode("companyAndProfession")
          .addConstraintViolation();

      return false;
    }

    return true;
  }
}
