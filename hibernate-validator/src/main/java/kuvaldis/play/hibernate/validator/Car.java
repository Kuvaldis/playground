package kuvaldis.play.hibernate.validator;

import kuvaldis.play.hibernate.validator.container.Gear;
import kuvaldis.play.hibernate.validator.container.GearBox;
import kuvaldis.play.hibernate.validator.container.MinTorque;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.List;

public class Car {

    @NotNull
    private String manufacturer;

    @NotNull
    @Size(min = 2, max = 14)
    private String licensePlate;

    @Min(2)
    private int seatCount;

    @AssertTrue(message = "The car has to pass the vehicle inspection first", groups = CarChecks.class)
    private boolean passedVehicleInspection;

    @Valid
    private Driver driver;

    private GearBox<@MinTorque(100) Gear> gearBox;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(final String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(final int seatCount) {
        this.seatCount = seatCount;
    }

    public GearBox<Gear> getGearBox() {
        return gearBox;
    }

    public void setGearBox(final GearBox<Gear> gearBox) {
        this.gearBox = gearBox;
    }

    public boolean isPassedVehicleInspection() {
        return passedVehicleInspection;
    }

    public void setPassedVehicleInspection(final boolean passedVehicleInspection) {
        this.passedVehicleInspection = passedVehicleInspection;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(final Driver driver) {
        this.driver = driver;
    }

    public void drive(final @Max(75) int speedInKmh) {

    }

}
