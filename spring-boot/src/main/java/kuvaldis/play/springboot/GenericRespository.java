package kuvaldis.play.springboot;

public interface GenericRespository<T> {
    void save(T input);
}
