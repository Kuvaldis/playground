package kuvaldis.play.lombok;

import org.junit.Test;

import static org.junit.Assert.*;

public class LombokMainTest {

    @Test
    public void testVal() {
        final String s = new LombokMain().method1();
        System.out.println(s);
    }

    @Test(expected = NullPointerException.class)
    public void nonNull() {
        final PersonHolder personHolder = new PersonHolder(null);
    }

    @Test(expected = Exception.class)
    public void throwException() {
        new ThrowException().throwException();
    }
}