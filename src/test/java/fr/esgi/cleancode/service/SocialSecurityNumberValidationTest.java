package fr.esgi.cleancode.service;

import fr.esgi.cleancode.validators.SocialSecurityNumberValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SocialSecurityNumberValidationTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "12345","123123ABC123123","11111111111111111" })
    void shouldReturnFalseForDrivingLicenseSocialSecurityNumber(String drivingLicenceSocialSecurityNumber) {
        Assertions.assertFalse(
                SocialSecurityNumberValidator.isValid(drivingLicenceSocialSecurityNumber)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789098765", "875429072342342" })
    void shouldReturnTrueForDrivingLicenseSocialSecurityNumber(String drivingLicenceSocialSecurityNumber) {
        Assertions.assertTrue(
                SocialSecurityNumberValidator.isValid(drivingLicenceSocialSecurityNumber)
        );
    }
}
