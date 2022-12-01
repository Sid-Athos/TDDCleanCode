package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.validators.SocialSecurityNumberValidator;

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

    protected DrivingLicence createDrivingLicenceFromSocialSecurityNumber(String driverToRegisterSocialSecurityNumber){
        UUID drivingLicenceId = drivingLicenceIdGenerationService.generateNewDrivingLicenceId();
        if(SocialSecurityNumberValidator.isValid(driverToRegisterSocialSecurityNumber)){
            var drivingLicenceToSave = DrivingLicence.builder()
                    .id(drivingLicenceId)
                    .driverSocialSecurityNumber(driverToRegisterSocialSecurityNumber)
                    .build();
            return inMemoryDrivingLicenceDatabase.save(drivingLicenceId, drivingLicenceToSave);
        }
        throw new InvalidDriverSocialSecurityNumberException("Must be 15 digits");
    }
}
