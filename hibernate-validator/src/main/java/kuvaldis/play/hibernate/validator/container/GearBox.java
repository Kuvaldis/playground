package kuvaldis.play.hibernate.validator.container;

public class GearBox<T extends Gear> {

    private final T gear;

    public GearBox(final T gear) {
        this.gear = gear;
    }

    public T getGear() {
        return gear;
    }
}
