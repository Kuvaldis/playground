package kuvaldis.play.gradle;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassToTestTest {

    @Test
    public void testMethodToTestShouldReturnBoo() throws Exception {
        System.out.println("I'm in test. Print me!");
        System.err.println("I'm in test. Print me, I'm acting like an error, but I'm not!");
        assertEquals("Boo", new ClassToTest().methodToTest());
    }
}