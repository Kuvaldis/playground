package kuvaldis.play.mapstruct.domain;

public class Person {

    private String firstName;

    private String lastName;

    private Car car;

    public Person() {
    }

    public Person(final String firstName, final String lastName, final Car car) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.car = car;
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

    public Car getCar() {
        return car;
    }

    public void setCar(final Car car) {
        this.car = car;
    }
}
