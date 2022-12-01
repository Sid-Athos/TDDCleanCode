package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.model.DrivingLicencePointsRange;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicencePointOperationService {
    private final InMemoryDatabase inMemoryDrivingLicenceDatabase;

    public DrivingLicence subtractPointsFromDrivingLicence(UUID drivingLicenceId, int pointsToSubtract) {
        Optional<DrivingLicence> drivingLicence = inMemoryDrivingLicenceDatabase.findById(drivingLicenceId);
        var foundDrivingLicence = drivingLicence.orElseThrow(() ->
                new ResourceNotFoundException("Driving Licence does not exist !")
        );
        var drivingLicenceAvailablePoints = foundDrivingLicence.getAvailablePoints();

        int subtractedPoints = computePointsSubtractionOnDrivingLicence(pointsToSubtract, drivingLicenceAvailablePoints);
        DrivingLicence drivingLicenceWithUpdatedPoints = DrivingLicence.updateDrivingLicencePoints(foundDrivingLicence, subtractedPoints);

        inMemoryDrivingLicenceDatabase.save(drivingLicenceId, drivingLicenceWithUpdatedPoints);
        return drivingLicenceWithUpdatedPoints;
    }

    private int computePointsSubtractionOnDrivingLicence(int pointsToSubtract, int availablePoints) {
        int subtractedPoints = availablePoints - pointsToSubtract;
        return Math.max(subtractedPoints, DrivingLicencePointsRange.MINIMUM.getRangeValue());
    }
}
