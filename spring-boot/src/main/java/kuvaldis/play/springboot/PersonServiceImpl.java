package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("personService")
public class PersonServiceImpl<T> {
    @Autowired
    private GenericRespository<T> repository;

    public void save(final T input) {
        repository.save(input);
    }
}
