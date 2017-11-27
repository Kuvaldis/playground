package kuvaldis.play.hibernate.validator;

import kuvaldis.play.hibernate.validator.container.Gear;
import kuvaldis.play.hibernate.validator.container.GearBox;
import kuvaldis.play.hibernate.validator.container.MinTorque;
import kuvaldis.play.hibernate.validator.custom.CaseMode;
import kuvaldis.play.hibernate.validator.custom.CheckCase;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Car {

    @NotNull
    private String manufacturer;

    @NotNull
    @Size(min = 2, max = 14)
    @CheckCase(CaseMode.UPPER)
    private String licensePlate;

    @Min(2)
    private int seatCount;

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
}
