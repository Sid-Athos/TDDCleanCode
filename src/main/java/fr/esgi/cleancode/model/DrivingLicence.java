package fr.esgi.cleancode.model;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.With;

import java.util.UUID;

@Value
@Builder
public class DrivingLicence {
    UUID id;

    String driverSocialSecurityNumber;

    @With
    @Default
    int availablePoints = DrivingLicencePointsRange.MAXIMUM.getRangeValue();


    public static DrivingLicence updateDrivingLicencePoints(DrivingLicence foundDrivingLicence, int subtractedPoints) {
        return foundDrivingLicence.withAvailablePoints(subtractedPoints);
    }
}
