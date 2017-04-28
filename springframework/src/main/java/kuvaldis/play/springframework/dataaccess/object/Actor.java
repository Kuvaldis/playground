package kuvaldis.play.springframework.dataaccess.object;

public class Actor {

    private final int id;
    private final String firstName;
    private final String lastName;

    public Actor(final int id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
