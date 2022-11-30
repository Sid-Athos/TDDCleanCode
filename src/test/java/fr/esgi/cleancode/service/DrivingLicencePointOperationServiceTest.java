package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DrivingLicencePointOperationServiceTest {

    @InjectMocks
    private DrivingLicencePointOperationService service;

    @Mock
    private InMemoryDatabase database;

    @Test
    void should_throw_resource_not_found_exception_when_not_found() {
        UUID uuid = UUID.randomUUID();
        int subtractedPoint = 1;

        Mockito.when(database.findById(uuid))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.subtract(uuid, subtractedPoint);
            Mockito.verify(database, Mockito.times(1)).findById(uuid);
            Mockito.verifyNoMoreInteractions();
        });
    }

    @Test
    void should_set_point_to_0_in_driver_licence() {

        UUID uuid = UUID.randomUUID();
        Optional<DrivingLicence> drivenLicenceToReturn = Optional.of(DrivingLicence.builder().id(uuid).build());
        int subtractedPoint = drivenLicenceToReturn.get().getAvailablePoints() + 1;

        Mockito.when(database.findById(uuid))
                .thenReturn(drivenLicenceToReturn);
        DrivingLicence returnedLicence = service.subtract(uuid, subtractedPoint);

        Mockito.verify(database, Mockito.times(1)).findById(uuid);
        Assertions.assertEquals(0, returnedLicence.getAvailablePoints());
    }

    @Test
    void should_set_point_to_6_in_driver_licence() {

        UUID uuid = UUID.randomUUID();
        Optional<DrivingLicence> drivenLicenceToReturn = Optional.of(DrivingLicence.builder().id(uuid).build());
        int subtractedPoint = drivenLicenceToReturn.get().getAvailablePoints() - 6;

        Mockito.when(database.findById(uuid))
                .thenReturn(drivenLicenceToReturn);
        DrivingLicence returnedLicence = service.subtract(uuid, subtractedPoint);

        Mockito.verify(database, Mockito.times(1)).findById(uuid);
        Assertions.assertEquals(6, returnedLicence.getAvailablePoints());
    }
}
