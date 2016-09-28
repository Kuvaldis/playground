package kuvaldis.play.mapstruct.dto;

public class CarDto {

    private String make;
    private int seatCount;
    private String type;

    public CarDto() {
    }

    public CarDto(final String make, final int seatCount, final String type) {
        this.make = make;
        this.seatCount = seatCount;
        this.type = type;
    }

    public String getMake() {
        return make;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public String getType() {
        return type;
    }

    public void setMake(final String make) {
        this.make = make;
    }

    public void setSeatCount(final int seatCount) {
        this.seatCount = seatCount;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
