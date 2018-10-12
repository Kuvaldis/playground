package kuvaldis.play.microprofile;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class BookService {

    public List<Book> getAll() {
        return Arrays.asList(
                new Book("A", "B"),
                new Book("1", "@")
        );
    }

}
