package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.model.DrivingLicencePointsRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DrivingLicencePointOperationServiceTest {

    @InjectMocks
    private DrivingLicencePointOperationService drivingLicencePointOperationService;

    @Mock
    private InMemoryDatabase inMemoryDrivingLicenceDatabase;
    @Captor
    ArgumentCaptor<DrivingLicence> drivingLicenceArgumentCaptor;
    @Test
    void shouldThrowResourceNotFoundExceptionWhenTryingToSubtractPointsButNoDrivingLicenceFound() {
        UUID uuid = UUID.randomUUID();
        int subtractedPoint = 1;

        Mockito.when(inMemoryDrivingLicenceDatabase.findById(uuid))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            drivingLicencePointOperationService.subtractPointsFromDrivingLicence(uuid, subtractedPoint);
            Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1)).findById(uuid);
            Mockito.verifyNoMoreInteractions();
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {13,14,15,16})
    void ShouldSetDrivingLicenceAvailablePointsToZeroWhenSubtractingPointsOverlappingARangeFromMinToMaxPoints(int drivingLicencePointsToSubtract) {

        UUID uuid = UUID.randomUUID();
        Optional<DrivingLicence> drivenLicenceToUseForFind = Optional.of(DrivingLicence.builder()
            .id(uuid)
            .build());
        DrivingLicence drivenLicenceToUseForSave = DrivingLicence.builder()
                .availablePoints(Math.max(DrivingLicencePointsRange.MAXIMUM.getRangeValue() - drivingLicencePointsToSubtract, 0))
                .id(uuid)
                .build();
        Mockito.when(inMemoryDrivingLicenceDatabase.findById(uuid))
                .thenReturn(drivenLicenceToUseForFind);
        Mockito.when(inMemoryDrivingLicenceDatabase.save(uuid,drivenLicenceToUseForSave))
                .thenReturn(drivenLicenceToUseForSave);


        DrivingLicence returnedLicence = drivingLicencePointOperationService.subtractPointsFromDrivingLicence(uuid, drivingLicencePointsToSubtract);

        Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1)).findById(uuid);
        Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1)).save(uuid,drivenLicenceToUseForSave);
        Mockito.verifyNoMoreInteractions(inMemoryDrivingLicenceDatabase);
        Assertions.assertEquals(0, returnedLicence.getAvailablePoints());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6})
    void shouldSubtractPointsFromExistingDrivingLicenceWithFullPoints(int pointsToRemoveFromDrivingLicence) {
        UUID uuid = UUID.randomUUID();
        Optional<DrivingLicence> drivenLicenceToReturn =
                Optional.of(DrivingLicence.builder().id(uuid).build());
        int subtractedPoints = drivenLicenceToReturn.get().getAvailablePoints() - pointsToRemoveFromDrivingLicence;
        Mockito.when(inMemoryDrivingLicenceDatabase.findById(uuid))
                .thenReturn(drivenLicenceToReturn);
        var updatedPointsDrivingLicence = drivenLicenceToReturn.get().withAvailablePoints(subtractedPoints);
        Mockito.when(inMemoryDrivingLicenceDatabase.save(uuid,updatedPointsDrivingLicence))
                .thenReturn(updatedPointsDrivingLicence);

        DrivingLicence returnedLicenceWithSubtractedPoints = drivingLicencePointOperationService.subtractPointsFromDrivingLicence(uuid, pointsToRemoveFromDrivingLicence);

        Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1)).findById(eq(uuid));
        Mockito.verify(inMemoryDrivingLicenceDatabase,Mockito.times(1)).save(eq(uuid), drivingLicenceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(inMemoryDrivingLicenceDatabase);
        Assertions.assertEquals(subtractedPoints, returnedLicenceWithSubtractedPoints.getAvailablePoints());
        Assertions.assertEquals(drivingLicenceArgumentCaptor.getValue().getId(), returnedLicenceWithSubtractedPoints.getId());
    }
}
