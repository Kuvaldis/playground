package kuvaldis.play.springframework.conversionservice;

import java.util.Set;

public class BeanWithSetOfStrings {

    private Set<String> strings;

    public Set<String> getStrings() {
        return strings;
    }

    public void setStrings(final Set<String> strings) {
        this.strings = strings;
    }
}
