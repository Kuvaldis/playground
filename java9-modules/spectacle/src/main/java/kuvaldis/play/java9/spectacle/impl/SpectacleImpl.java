package kuvaldis.play.java9.spectacle.impl;

import kuvaldis.play.java9.actors.Actor;
//import kuvaldis.play.java9.actors.impl.ActorImpl;
import kuvaldis.play.java9.spectacle.Spectacle;

import java.util.ArrayList;
import java.util.List;

public class SpectacleImpl implements Spectacle {

    private final List<Actor> actors = new ArrayList<>();

    @Override
    public List<Actor> allActors() {
        return actors;
    }

    @Override
    public void assignActor(final Actor actor) {
//        new ActorImpl() // <- illegal, it's not exported by kuvaldis.play.java9.actors module
        actors.add(actor);
    }

    @Override
    public void play() {
        for (Actor actor : actors) {
            actor.act();
        }
    }
}
