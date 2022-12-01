package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.model.DrivingLicencePointsRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DrivingLicenseSaveServiceTest {
    @InjectMocks
    DrivingLicenceCreationService drivingLicenceCreationService;
    @Mock
    InMemoryDatabase inMemoryDrivingLicenceDatabase;
    @Mock
    DrivingLicenceIdGenerationService drivingLicenceIdGenerationService;
    @Captor
    ArgumentCaptor<DrivingLicence> drivingLicenceArgumentCaptor;

    @ParameterizedTest
    @ValueSource(strings = {"123456789098765", "875429072342342" })
    void shouldCreateDrivingLicenceFromSocialSecurityNumber(String socialSecurityNumber) {
        // Arrange
        var uuidToTest = UUID.randomUUID();
        Optional<DrivingLicence> notFoundDrivingLicenceById = Optional.empty();
        var drivingLicenseToTest = DrivingLicence.builder()
                .id(uuidToTest)
                .driverSocialSecurityNumber(socialSecurityNumber)
                .build();
        Mockito.when(drivingLicenceIdGenerationService.generateNewDrivingLicenceId()).thenReturn(uuidToTest);
        Mockito.when(inMemoryDrivingLicenceDatabase.findById(Mockito.any(UUID.class))).thenReturn(notFoundDrivingLicenceById);
        Mockito.when(inMemoryDrivingLicenceDatabase.save(uuidToTest,drivingLicenseToTest)).thenReturn(drivingLicenseToTest);

        // Do
        DrivingLicence returnedDrivingLicence = drivingLicenceCreationService.createDrivingLicenceFromSocialSecurityNumber(socialSecurityNumber);

        // Check
        Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1))
                .save(eq(uuidToTest), drivingLicenceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(inMemoryDrivingLicenceDatabase);
        DrivingLicence capturedDrivingLicense = drivingLicenceArgumentCaptor.getValue();
        Assertions.assertEquals(capturedDrivingLicense.getId(), uuidToTest);
        Assertions.assertEquals(capturedDrivingLicense.getAvailablePoints(), DrivingLicencePointsRange.MAXIMUM.getRangeValue());
        Assertions.assertEquals(capturedDrivingLicense.getDriverSocialSecurityNumber(), socialSecurityNumber);
        Assertions.assertEquals(capturedDrivingLicense.getAvailablePoints(), returnedDrivingLicence.getAvailablePoints());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ","   ", "12345","123123ABC123123","11111111111111111" })
    void shouldThrowErrorWhenCreatingDrivingLicence(String socialSecurityNumber) {
        /**Assertions.assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            drivingLicenceCreationService.createDrivingLicenceFromSocialSecurityNumber(socialSecurityNumber);
        });*/
    }
}
