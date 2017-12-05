package kuvaldis.play.java9.spectacle;

import kuvaldis.play.java9.actors.Actor;
import kuvaldis.play.java9.spectacle.impl.SpectacleImpl;

import java.util.List;

public interface Spectacle {

    static Spectacle create() {
        return new SpectacleImpl();
    }

    List<Actor> allActors();

    void assignActor(Actor actor);

    void play();
}
