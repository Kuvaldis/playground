package kuvaldis.play.mapstruct.domain;

public class Address {

    private String city;

    private String addressLine1;

    public Address() {
    }

    public Address(final String city, final String addressLine1) {
        this.city = city;
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }
}
