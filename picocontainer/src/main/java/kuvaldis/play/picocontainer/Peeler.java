package kuvaldis.play.picocontainer;

import org.picocontainer.Startable;

public class Peeler implements Startable {

    private final Peelable peelable;

    public Peeler(Peelable peelable) {
        this.peelable = peelable;
    }

    @Override
    public void start() {
        peelable.peel();
    }

    @Override
    public void stop() {

    }


}
