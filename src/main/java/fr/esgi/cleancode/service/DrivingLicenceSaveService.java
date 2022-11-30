package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;

import java.util.UUID;

public class DrivingLicenceSaveService {


    private final InMemoryDatabase inMemoryDatabase;

    private final DrivingLicenceIdGenerationService drivingLicenceIdGenerationService;

    public DrivingLicenceSaveService(InMemoryDatabase inMemoryDatabase, DrivingLicenceIdGenerationService drivingLicenceIdGenerationService){
        this.inMemoryDatabase = inMemoryDatabase;
        this.drivingLicenceIdGenerationService = drivingLicenceIdGenerationService;
    }

    public DrivingLicence createDrivingLicence(String drivingSocialSecurityNumber){
        if(!isValidSocialSecurityNumber(drivingSocialSecurityNumber)){
            throw new InvalidDriverSocialSecurityNumberException("Social Number is invalid. Must be 15 digits");
        }
        DrivingLicence toCreate = DrivingLicence.builder()
                .id(drivingLicenceIdGenerationService.generateNewDrivingLicenceId())
                .driverSocialSecurityNumber(drivingSocialSecurityNumber).build();
        return inMemoryDatabase.save(toCreate.getId(), toCreate);
    }

    protected Boolean isValidSocialSecurityNumber(String drivingSocialSecurityNumber){
        if(drivingSocialSecurityNumber == null){
            return false;
        }
        return drivingSocialSecurityNumber.matches("^[0-9]{15}$");
    }
}
