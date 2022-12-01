package fr.esgi.cleancode.exception;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InvalidDriverSocialSecurityNumberException extends ConstraintDeclarationException {
    public InvalidDriverSocialSecurityNumberException(String message) {
        super(message);
    }
}
