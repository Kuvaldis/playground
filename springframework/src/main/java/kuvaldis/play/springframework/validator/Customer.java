package kuvaldis.play.springframework.validator;

public class Customer {

    private final String name;

    private final int age;

    private final Address address;

    public Customer(final String name, final int age, final Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }
}
