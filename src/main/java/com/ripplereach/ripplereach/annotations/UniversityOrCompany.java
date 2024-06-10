package com.ripplereach.ripplereach.annotations;

import com.ripplereach.ripplereach.annotations.validators.UniversityOrCompanyValidator;
import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniversityOrCompanyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniversityOrCompany {
  String message() default Messages.COMPANY_OR_UNIVERSITY_REQUIRED;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
