package com.ripplereach.ripplereach.annotations;

import com.ripplereach.ripplereach.annotations.validators.UniversityOrCompanyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniversityOrCompanyValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniversityOrCompany {
  String message() default "Either university or company must be present, but not both.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
