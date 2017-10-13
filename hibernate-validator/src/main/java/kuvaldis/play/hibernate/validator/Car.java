package kuvaldis.play.hibernate.validator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Car {

    @NotNull
    private final String manufacturer;

    @NotNull
    @Size(min = 2, max = 14)
    private final String licensePlate;

    @Min(2)
    private final int seatCount;

    public Car(@NotNull final String manufacturer, @NotNull @Size(min = 2, max = 14) final String licensePlate, @Min(2) final int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }
}
