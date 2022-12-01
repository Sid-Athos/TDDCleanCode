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

@ExtendWith(MockitoExtension.class)
class DrivingLicenceFinderServiceTest {

    @InjectMocks
    private DrivingLicenceFinderService drivingLicenceFinderService;

    @Mock
    private InMemoryDatabase inMemoryDrivingLicenceDatabase;

    @Captor
    ArgumentCaptor<UUID> drivingLicenceUuidArgumentCaptor;

    @Test
    void shouldFindDrivingLicenseById() {
        UUID uuidToTest = UUID.randomUUID();
        Optional<DrivingLicence> drivenLicenceToReturn = Optional.of(DrivingLicence.builder().id(uuidToTest).build());
        Mockito.when(inMemoryDrivingLicenceDatabase.findById(uuidToTest))
                .thenReturn(drivenLicenceToReturn);

        Optional<DrivingLicence> returnedLicence = drivingLicenceFinderService.findById(uuidToTest);

        Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1))
                .findById(drivingLicenceUuidArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(inMemoryDrivingLicenceDatabase);
        Assertions.assertTrue(returnedLicence.isPresent());
        Assertions.assertEquals(12, returnedLicence.get().getAvailablePoints());
        Assertions.assertEquals(drivingLicenceUuidArgumentCaptor.getValue(), returnedLicence.get().getId());
    }

    @Test
    void shouldNotFindDrivingLicenseById() {
        UUID uuidToTest = UUID.randomUUID();
        Mockito.when(inMemoryDrivingLicenceDatabase.findById(uuidToTest))
                .thenReturn(Optional.empty());

        Optional<DrivingLicence> returnedLicence = drivingLicenceFinderService.findById(uuidToTest);

        Mockito.verify(inMemoryDrivingLicenceDatabase, Mockito.times(1))
                .findById(drivingLicenceUuidArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(inMemoryDrivingLicenceDatabase);
        Assertions.assertTrue(returnedLicence.isEmpty());
        Assertions.assertEquals(drivingLicenceUuidArgumentCaptor.getValue(), uuidToTest);
    }
}