package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

public class DrivingLicenseSaveServiceTest {
    @InjectMocks
    DrivingLicenceSaveService drivingLicenceSaveService;

    @Mock
    InMemoryDatabase inMemoryDatabase;

    @Captor
    ArgumentCaptor<DrivingLicence> drivingLicenceArgumentCaptor;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "12345","123123ABC123123","11111111111111111" })
    void shouldThrowErrorWhenCreateDrivingLicenceWithValidSocialSecurityNumber(String drivingLicenceSocialSecurityNumber) {
        Mockito.when(drivingLicenceSaveService.isValidSocialSecurityNumber(drivingLicenceSocialSecurityNumber)).thenThrow(InvalidDriverSocialSecurityNumberException.class);
        Assertions.assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            drivingLicenceSaveService.isValidSocialSecurityNumber(drivingLicenceSocialSecurityNumber);
        });
        Mockito.verify(drivingLicenceSaveService, Mockito.times(1));
        Mockito.verifyNoMoreInteractions(drivingLicenceSaveService.isValidSocialSecurityNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789098765", "875429072342342" })
    void shouldThrowErrorWhenCreateDrivingLicenceWithValidSocialSecurityNumber(String drivingLicenceSocialSecurityNumber) {
        Mockito.when(drivingLicenceSaveService.isValidSocialSecurityNumber(drivingLicenceSocialSecurityNumber)).thenThrow(InvalidDriverSocialSecurityNumberException.class);
        Assertions.assertDoesNotThrow( () -> {
            drivingLicenceSaveService.isValidSocialSecurityNumber(drivingLicenceSocialSecurityNumber);
        });
        Mockito.verify(drivingLicenceSaveService, Mockito.times(1));
        Mockito.verifyNoMoreInteractions(drivingLicenceSaveService.isValidSocialSecurityNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789098765", "875429072342342" })
    void shouldCreateDrivingLicenceAndVerifyValues(String drivingLicenceSocialSecurityNumber) {
        Mockito.when(drivingLicenceSaveService.saveDrivingLicence(drivingLicenceSocialSecurityNumber)).thenReturn(Mockito.any());
        drivingLicenceSaveService.saveDrivingLicence(drivingLicenceSocialSecurityNumber);
        Mockito.verify(drivingLicenceSaveService, Mockito.times(1)).saveDrivingLicence(drivingLicenceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(drivingLicenceSaveService.saveDrivingLicence);
        DrivingLicence capturedDrivingLicense = drivingLicenceArgumentCaptor.getValue();
        Assertions.assertEquals(capturedDrivingLicense.getDriverSocialSecurityNumber(), drivingLicenceSocialSecurityNumber);
        Assertions.assertEquals(capturedDrivingLicense.getAvailablePoints(), 12);
    }
}
