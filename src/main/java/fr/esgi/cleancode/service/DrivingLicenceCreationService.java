package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.model.DrivingLicence;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

class DrivingLicenceCreationService {
    private final InMemoryDatabase inMemoryDrivingLicenceDatabase;
    private final DrivingLicenceIdGenerationService drivingLicenceIdGenerationService;

    public DrivingLicenceCreationService(
            InMemoryDatabase inMemoryDrivingLicenceDatabase,
            DrivingLicenceIdGenerationService drivingLicenceIdGenerationService){
        this.inMemoryDrivingLicenceDatabase = inMemoryDrivingLicenceDatabase;
        this.drivingLicenceIdGenerationService = drivingLicenceIdGenerationService;
    }


     /** @see SocialSecurityNumberValidator for social security number validation */
    protected DrivingLicence createDrivingLicenceFromSocialSecurityNumber(String driverToRegisterSocialSecurityNumber){
        UUID drivingLicenceId = drivingLicenceIdGenerationService.generateNewDrivingLicenceId();
        // Because crappy ID generation system
        inMemoryDrivingLicenceDatabase.findById(drivingLicenceId).ifPresent(drivingLicence -> {
            throw new ResourceAccessException("Crappy DB already has a driving licence. " + drivingLicence);
        });
        var drivingLicenceToSave = DrivingLicence.builder()
                .id(drivingLicenceId)
                .driverSocialSecurityNumber(driverToRegisterSocialSecurityNumber)
                .build();
        return inMemoryDrivingLicenceDatabase.save(drivingLicenceId, drivingLicenceToSave);
    }
}
