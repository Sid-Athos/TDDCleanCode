package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;

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
        // Because crappy ID generation system
        if(inMemoryDrivingLicenceDatabase.findById(drivingLicenceId).isEmpty()){
            var drivingLicenceToSave = DrivingLicence.builder()
                    .id(drivingLicenceId)
                    .driverSocialSecurityNumber(driverToRegisterSocialSecurityNumber)
                    .build();
            return inMemoryDrivingLicenceDatabase.save(drivingLicenceId, drivingLicenceToSave);
        }
        throw new InvalidDriverSocialSecurityNumberException("Social Number is invalid. Must be 15 digits");
    }
}
