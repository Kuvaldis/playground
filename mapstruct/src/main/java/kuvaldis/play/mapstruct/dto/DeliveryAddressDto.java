package kuvaldis.play.mapstruct.dto;

public class DeliveryAddressDto {

    private String city;

    private String address;

    private String firstName;

    private String lastName;

    private CarDto carDto;

    public DeliveryAddressDto() {
    }

    public DeliveryAddressDto(final String city, final String address, final String firstName, final String lastName, final CarDto carDto) {
        this.city = city;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.carDto = carDto;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public CarDto getCarDto() {
        return carDto;
    }

    public void setCarDto(final CarDto carDto) {
        this.carDto = carDto;
    }
}
