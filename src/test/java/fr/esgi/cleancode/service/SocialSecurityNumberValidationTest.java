package fr.esgi.cleancode.service;

import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SocialSecurityNumberValidationTest {
    @Test
    void testValidation(){

        Assertions.assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            SocialSecurityNumberValidator withWrongData = new SocialSecurityNumberValidator();
            withWrongData.isValid(" ", null);

        });
    }


}
