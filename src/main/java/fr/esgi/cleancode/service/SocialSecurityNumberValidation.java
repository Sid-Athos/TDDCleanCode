package fr.esgi.cleancode.service;


import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({PARAMETER,FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = SocialSecurityNumberValidator.class)
public @interface SocialSecurityNumberValidation {
    String message() default "Must be 15 digits";
}
