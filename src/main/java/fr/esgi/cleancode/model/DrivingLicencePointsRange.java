package fr.esgi.cleancode.model;

public enum DrivingLicencePointsRange {
    MINIMUM(0),
    MAXIMUM(12);
    private final int rangeValue;
    DrivingLicencePointsRange(int rangeValue) {
        this.rangeValue = rangeValue;
    }

    public int getRangeValue(){
        return this.rangeValue;
    }
}
