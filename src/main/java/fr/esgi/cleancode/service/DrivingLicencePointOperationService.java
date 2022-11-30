package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicencePointOperationService {

    private final InMemoryDatabase database;

    public DrivingLicence subtract(UUID drivingLicenceId, int point) throws ResourceNotFoundException {
        Optional<DrivingLicence> drivingLicence = database.findById(drivingLicenceId);

        if (drivingLicence.isEmpty()) {
            throw new ResourceNotFoundException("Driving Licence Not found !");
        }

        int sub = drivingLicence.get().getAvailablePoints() - point;
        if (sub < 0 )
            sub = 0;

        DrivingLicence drivingLicenceModified = DrivingLicence.builder().id(drivingLicenceId)
                .driverSocialSecurityNumber(drivingLicence.get()
                .getDriverSocialSecurityNumber())
                .availablePoints(sub)
                .build();

        database.save(drivingLicenceId, drivingLicenceModified);
        return drivingLicenceModified;
    }
}
