package com.azurep1.p1be.limitation.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodGreaterThanLimitationValidator implements ConstraintValidator<PeriodGreaterThanLimitation, PeriodAndLimitationDto> {

   public boolean isValid(PeriodAndLimitationDto dto, ConstraintValidatorContext context) {
      return dto.getLimitationSeconds() == null ||
              dto.getPeriodSeconds() == null ||
              dto.getLimitationSeconds() < dto.getPeriodSeconds();
   }
}
