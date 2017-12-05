package kuvaldis.play.java9.actors.impl;

import kuvaldis.play.java9.actors.Actor;

public class ActorImpl implements Actor {

    private final String name;

    private final String action;

    public ActorImpl(final String name, final String action) {
        this.name = name;
        this.action = action;
    }

    @Override
    public void act() {
        System.out.println(name + ": " + action);
    }
}
