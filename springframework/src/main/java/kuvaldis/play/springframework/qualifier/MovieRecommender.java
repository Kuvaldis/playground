package kuvaldis.play.springframework.qualifier;

import org.springframework.beans.factory.annotation.Autowired;

public class MovieRecommender {

    @Autowired
    @MovieQualifier(format = Format.VHS, genre = "comedy")
    private MovieCatalog vhsComedies;

    @Autowired
    @MovieQualifier(format = Format.DVD, genre = "action")
    private MovieCatalog dvdActions;

    @Autowired
    @MovieQualifier(format = Format.BLUERAY, genre = "drama")
    private MovieCatalog bluerayDramas;

    public String getComedy() {
        return vhsComedies.getMovie();
    }

    public String getAction() {
        return dvdActions.getMovie();
    }

    public String getDrama() {
        return bluerayDramas.getMovie();
    }
}
