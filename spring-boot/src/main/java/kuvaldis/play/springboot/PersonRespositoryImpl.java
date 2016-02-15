package kuvaldis.play.springboot;

import org.springframework.stereotype.Repository;

@Repository
public class PersonRespositoryImpl implements GenericRespository<Person> {
    @Override
    public void save(final Person input) {
        System.out.println("Save person");
    }
}
