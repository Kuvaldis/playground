package kuvaldis.play.picocontainer;

import org.picocontainer.injectors.Provider;

public class FruitSaladProvider implements Provider {
    public FruitSalad provide(final Apple apple, final Banana banana, final Orange orange) {
        return new FruitSalad(apple, banana, orange);
    }
}
