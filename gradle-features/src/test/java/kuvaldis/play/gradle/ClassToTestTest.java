package kuvaldis.play.gradle;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassToTestTest {

    @Test
    public void testMethodToTestShouldReturnBoo() throws Exception {
        assertEquals("Boo", new ClassToTest().methodToTest());
    }
}