package kuvaldis.play.springframework.propertyeditor;

public class Person2 {

    private final String firstName;

    private final String lastName;

    public Person2(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
