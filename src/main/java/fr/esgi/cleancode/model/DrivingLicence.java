package fr.esgi.cleancode.model;

import fr.esgi.cleancode.service.SocialSecurityNumberValidation;
import fr.esgi.cleancode.service.SocialSecurityNumberValidator;
import io.vavr.match.annotation.Patterns;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.With;

import java.util.UUID;

@Value
@Builder
public class DrivingLicence {
    UUID id;

    @SocialSecurityNumberValidation
    String driverSocialSecurityNumber;

    @With
    @Default
    int availablePoints = DrivingLicencePointsRange.MAXIMUM.getRangeValue();


    public static DrivingLicence updateDrivingLicencePoints(DrivingLicence foundDrivingLicence, int subtractedPoints) {
        return foundDrivingLicence.withAvailablePoints(subtractedPoints);
    }

    public static DrivingLicence updateDrivingLicenceId(DrivingLicence drivingLicenceToUpdate, UUID newId){
        return DrivingLicence.builder()
                .availablePoints(drivingLicenceToUpdate.getAvailablePoints())
                .id(newId)
                .build();
    }
}
