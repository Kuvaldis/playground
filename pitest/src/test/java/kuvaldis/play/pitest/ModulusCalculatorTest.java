package kuvaldis.play.pitest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModulusCalculatorTest {

    @Test
    public void testSum() throws Exception {
        assertEquals(4, new ModulusCalculator().sum(1, 3).longValue());
        assertEquals(4, new ModulusCalculator(5).sum(1, 3).longValue());
        assertEquals(4, new ModulusCalculator().sum(11, 3).longValue()); // comment this row and test will fail
        // it means the case of actual modulus division wasn't checked, so even though code coverage is 100% it is
        // not a correct test without the row
    }
}