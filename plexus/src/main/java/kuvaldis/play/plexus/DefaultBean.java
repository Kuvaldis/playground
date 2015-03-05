package kuvaldis.play.plexus;

public class DefaultBean implements Bean {

    private Integer integer;

    @Override
    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    @Override
    public Integer getInteger() {
        return integer;
    }
}
