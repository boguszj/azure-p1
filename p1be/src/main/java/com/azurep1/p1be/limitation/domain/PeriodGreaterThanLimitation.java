package com.azurep1.p1be.limitation.domain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PeriodGreaterThanLimitationValidator.class)
public @interface PeriodGreaterThanLimitation {

    String message() default "Period must be greater than limit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}