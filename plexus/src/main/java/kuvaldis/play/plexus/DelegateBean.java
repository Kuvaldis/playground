package kuvaldis.play.plexus;

public class DelegateBean implements Bean {

    private Bean bean;

    @Override
    public void setInteger(Integer integer) {
        bean.setInteger(integer);
    }

    @Override
    public Integer getInteger() {
        return bean.getInteger();
    }
}
