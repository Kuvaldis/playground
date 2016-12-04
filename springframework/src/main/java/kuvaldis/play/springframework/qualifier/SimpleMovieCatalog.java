package kuvaldis.play.springframework.qualifier;

public class SimpleMovieCatalog implements MovieCatalog {

    private String movieName;

    @Override
    public String getMovie() {
        return movieName;
    }

    public void setMovieName(final String movieName) {
        this.movieName = movieName;
    }
}
