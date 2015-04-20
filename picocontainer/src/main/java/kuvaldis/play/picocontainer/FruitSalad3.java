package kuvaldis.play.picocontainer;

import javax.inject.Inject;

public class FruitSalad3 {

    @Inject
    private Apple apple;
    @Inject
    private Banana banana;
    @Inject
    private Orange orange;

    public Apple getApple() {
        return apple;
    }

    public Banana getBanana() {
        return banana;
    }

    public Orange getOrange() {
        return orange;
    }
}