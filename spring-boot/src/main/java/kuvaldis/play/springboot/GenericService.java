package kuvaldis.play.springboot;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericService<T> {

    @Autowired
    private GenericRespository<T> respository;

    public void saveProduct(T input) {
        respository.save(input);
    }
}
