package kuvaldis.play.mapstruct.domain;

public class Car {

    private String make;
    private int numberOfSeats;
    private CarType type;
    private double price;

    public Car() {
    }

    public Car(final String make, final int numberOfSeats, final CarType type, final double price) {
        this.make = make;
        this.numberOfSeats = numberOfSeats;
        this.type = type;
        this.price = price;
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

    public double getPrice() {
        return price;
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

    public void setPrice(final double price) {
        this.price = price;
    }
}
