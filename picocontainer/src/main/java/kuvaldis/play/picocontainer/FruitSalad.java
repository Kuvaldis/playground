package kuvaldis.play.picocontainer;

public class FruitSalad {

    private final Apple apple;
    private final Banana banana;
    private final Orange orange;

    public FruitSalad(Apple apple, Banana banana, Orange orange) {
        this.apple = apple;
        this.banana = banana;
        this.orange = orange;
    }

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
