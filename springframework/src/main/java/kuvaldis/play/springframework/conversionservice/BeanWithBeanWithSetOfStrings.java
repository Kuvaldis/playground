package kuvaldis.play.springframework.conversionservice;

public class BeanWithBeanWithSetOfStrings {

    private BeanWithSetOfStrings bean;

    public BeanWithSetOfStrings getBean() {
        return bean;
    }

    public void setBean(final BeanWithSetOfStrings bean) {
        this.bean = bean;
    }
}
