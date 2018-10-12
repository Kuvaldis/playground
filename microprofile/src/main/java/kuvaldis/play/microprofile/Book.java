package kuvaldis.play.microprofile;

public class Book {

    private final String title;

    private final String author;

    public Book(final String title, final String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
