package kuvaldis.play.microprofile;

import java.util.List;

public class Books {

    private final List<Book> books;

    public Books(final List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }
}
