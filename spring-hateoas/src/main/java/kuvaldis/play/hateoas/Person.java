package kuvaldis.play.hateoas;

import org.springframework.hateoas.Identifiable;

public class Person implements Identifiable<Long> {

    private final Long id;

    String firstname;

    String lastname;

    public Person(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
}
