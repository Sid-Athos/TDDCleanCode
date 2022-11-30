package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)

public class DrivingLicenseSaveServiceTest {
    @InjectMocks
    DrivingLicenceSaveService drivingLicenceSaveService;

    @Mock
    InMemoryDatabase inMemoryDatabase;

    @Mock
    DrivingLicenceIdGenerationService drivingLicenceIdGenerationService;


    @Captor
    ArgumentCaptor<DrivingLicence> drivingLicenceArgumentCaptor;


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "12345","123123ABC123123","11111111111111111" })
    void shouldThrowErrorWhenCreateDrivingLicenceWithValidSocialSecurityNumber(String drivingLicenceSocialSecurityNumber) {
        Assertions.assertFalse(
            drivingLicenceSaveService.isValidSocialSecurityNumber(drivingLicenceSocialSecurityNumber)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789098765", "875429072342342" })
    void shouldNotThrowErrorWhenCreateDrivingLicenceWithValidSocialSecurityNumber(String drivingLicenceSocialSecurityNumber) {
        Assertions.assertTrue(
            drivingLicenceSaveService.isValidSocialSecurityNumber(drivingLicenceSocialSecurityNumber)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789098765", "875429072342342" })
    void shouldCreateDrivingLicenceAndVerifyValues(String drivingLicenceSocialSecurityNumber) {
        UUID uidToUse = UUID.randomUUID();
        DrivingLicence dlToUse = DrivingLicence.builder().id(uidToUse).driverSocialSecurityNumber(drivingLicenceSocialSecurityNumber).build();
        Mockito.when(drivingLicenceIdGenerationService.generateNewDrivingLicenceId()).thenReturn(uidToUse);
        Mockito.when(inMemoryDatabase.save(uidToUse,dlToUse)).thenReturn(dlToUse);
        drivingLicenceSaveService.createDrivingLicence(drivingLicenceSocialSecurityNumber);
        Mockito.verify(inMemoryDatabase, Mockito.times(1)).save(eq(uidToUse), drivingLicenceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(inMemoryDatabase);
        DrivingLicence capturedDrivingLicense = drivingLicenceArgumentCaptor.getValue();
        Assertions.assertEquals(capturedDrivingLicense.getId(), uidToUse);
        Assertions.assertEquals(capturedDrivingLicense.getAvailablePoints(), 12);
        Assertions.assertEquals(capturedDrivingLicense.getDriverSocialSecurityNumber(), drivingLicenceSocialSecurityNumber);

    }
}
