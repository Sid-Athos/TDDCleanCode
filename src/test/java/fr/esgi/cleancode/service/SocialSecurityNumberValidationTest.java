package fr.esgi.cleancode.service;

import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SocialSecurityNumberValidationTest {
    @Test
    void testValidation(){

        Assertions.assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            DrivingLicence withWrongData = DrivingLicence.builder().driverSocialSecurityNumber(" ").build();

        });
    }


}
