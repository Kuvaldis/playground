package kuvaldis.play.picocontainer;

public class Juicer {

    private final Peeler peeler;
    private final Peelable peelable;

    public Juicer(Peeler peeler, Peelable peelable) {
        this.peeler = peeler;
        this.peelable = peelable;
    }

    public Peeler getPeeler() {
        return peeler;
    }

    public Peelable getPeelable() {
        return peelable;
    }
}
