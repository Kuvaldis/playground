package kuvaldis.play.hibernate.validator.container;

public class Gear {

    private final Integer torque;

    public Gear(final Integer torque) {
        this.torque = torque;
    }

    public Integer getTorque() {
        return torque;
    }

    public static class OldGear extends Gear {

        public OldGear() {
            super(10);
        }
    }
}
