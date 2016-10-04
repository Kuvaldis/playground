package kuvaldis.play.mapstruct.dto;

public class CarDto {

    private String make;
    private int seatCount;
    private String type;
    private String price;

    public CarDto() {
    }

    public CarDto(final String make, final int seatCount, final String type, final String price) {
        this.make = make;
        this.seatCount = seatCount;
        this.type = type;
        this.price = price;
    }

    public String getMake() {
        return make;
    }

    public void setMake(final String make) {
        this.make = make;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(final int seatCount) {
        this.seatCount = seatCount;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }
}
