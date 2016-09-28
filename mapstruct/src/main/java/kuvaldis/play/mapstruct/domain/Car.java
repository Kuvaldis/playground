package kuvaldis.play.mapstruct.domain;

public class Car {

    private String make;
    private int numberOfSeats;
    private CarType type;

    public Car() {
    }

    public Car(final String make, final int numberOfSeats, final CarType type) {
        this.make = make;
        this.numberOfSeats = numberOfSeats;
        this.type = type;
    }

    public String getMake() {
        return make;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public CarType getType() {
        return type;
    }

    public void setMake(final String make) {
        this.make = make;
    }

    public void setNumberOfSeats(final int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setType(final CarType type) {
        this.type = type;
    }
}
