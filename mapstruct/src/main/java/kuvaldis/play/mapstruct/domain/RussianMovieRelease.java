package kuvaldis.play.mapstruct.domain;

public class RussianMovieRelease {

    private String title;

    public RussianMovieRelease() {
    }

    public RussianMovieRelease(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}
