package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.atMostOnce;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceFinderServiceTest {

    @InjectMocks
    private DrivingLicenceFinderService service;

    @Mock
    private InMemoryDatabase database;

    @Test
    void should_find() {

        Optional<DrivingLicence> drivenLicenceToReturn = Optional.of(DrivingLicence.builder().build());
        UUID uuid = UUID.randomUUID();

        Mockito.when(database.findById(uuid))
                .thenReturn(drivenLicenceToReturn);
        Optional<DrivingLicence> returnedLicence = service.findById(uuid);

        Mockito.verify(database, Mockito.times(1)).findById(uuid);
        Assertions.assertTrue(returnedLicence.isPresent());
        Assertions.assertEquals(12, returnedLicence.get().getAvailablePoints());
    }

    @Test
    void should_not_find() {

        Optional<DrivingLicence> drivenLicenceToReturn = Optional.of(DrivingLicence.builder().build());
        UUID uuid = UUID.randomUUID();

        Mockito.when(database.findById(uuid))
                .thenReturn(Optional.empty());
        Optional<DrivingLicence> returnedLicence = service.findById(uuid);

        Mockito.verify(database, Mockito.times(1)).findById(uuid);
        Assertions.assertTrue(returnedLicence.isEmpty());
    }
}