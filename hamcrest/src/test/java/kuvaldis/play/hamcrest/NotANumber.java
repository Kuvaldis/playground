package kuvaldis.play.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

public class NotANumber extends TypeSafeMatcher<Double> {

    @Override
    protected boolean matchesSafely(Double item) {
        return item.isNaN();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("not a number");
    }

    @Factory
    public static NotANumber notANumber() {
        return new NotANumber();
    }
}
