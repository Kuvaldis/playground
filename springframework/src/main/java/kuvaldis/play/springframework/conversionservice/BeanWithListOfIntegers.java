package kuvaldis.play.springframework.conversionservice;

import java.util.List;

public class BeanWithListOfIntegers {

    private List<Integer> integers;

    public List<Integer> getIntegers() {
        return integers;
    }

    public void setIntegers(final List<Integer> integers) {
        this.integers = integers;
    }
}
