package kuvaldis.play.springframework.propertyeditor;

public class BeanWithPersons {

    private Person1 person1;

    private Person2 person2;

    public Person1 getPerson1() {
        return person1;
    }

    public void setPerson1(final Person1 person1) {
        this.person1 = person1;
    }

    public Person2 getPerson2() {
        return person2;
    }

    public void setPerson2(final Person2 person2) {
        this.person2 = person2;
    }
}
