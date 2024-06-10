package com.ripplereach.ripplereach.annotations;

import com.ripplereach.ripplereach.annotations.validators.CompanyAndProfessionValidator;
import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CompanyAndProfessionValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyAndProfession {
  String message() default Messages.PROFESSION_REQUIRED;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
