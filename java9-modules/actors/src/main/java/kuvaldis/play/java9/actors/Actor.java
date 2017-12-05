package kuvaldis.play.java9.actors;

import kuvaldis.play.java9.actors.impl.ActorImpl;

public interface Actor {

    void act();

    static Actor withPhrase(final String name, final String phrase) {
        return new ActorImpl(name, "\"" + phrase + "\"");
    }

    static Actor withAction(final String name, final String action) {
        return new ActorImpl(name, "*" + action + "*");
    }
}
