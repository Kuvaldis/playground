package kuvaldis.play.springframework.validator;

public class Address {

    private final String addressLine1;

    private final String addressLine2;

    public Address(final String addressLine1, final String addressLine2) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }
}
