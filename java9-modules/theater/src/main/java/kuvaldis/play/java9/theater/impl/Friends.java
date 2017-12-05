package kuvaldis.play.java9.theater.impl;

import kuvaldis.play.java9.actors.Actor;
import kuvaldis.play.java9.spectacle.Spectacle;
import kuvaldis.play.java9.theater.Theater;

public class Friends implements Theater {
    @Override
    public void playSpectacle() {
        final Spectacle spectacle = Spectacle.create();

        final Actor ross = Actor.withPhrase("Ross", "Joey, dance!");
        final Actor joey = Actor.withAction("Joey", "dances, but it does not help");

        spectacle.assignActor(ross);
        spectacle.assignActor(joey);

        spectacle.play();
    }
}
